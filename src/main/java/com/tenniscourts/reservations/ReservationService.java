package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScheduleService scheduleService;
    private final GuestService guestService;

    private final ReservationMapper reservationMapper;
    private final ScheduleMapper scheduleMapper;
    private final GuestMapper guestMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        var reserve = reservationMapper.map(createReservationRequestDTO);
        var toCreate = Reservation.builder()
                .schedule(scheduleMapper.map(scheduleService.findSchedule(createReservationRequestDTO.getScheduleId())))
                .guest(guestMapper.map(guestService.findById(createReservationRequestDTO.getGuestId())))
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .value(reserve.getValue())
                .refundValue(getRefundValue(reserve))
                .build();
        log.info("[RESERVATION-SERVICE] - Success to create a new reservation: {}.", toCreate);
        return reservationMapper.map(reservationRepository.saveAndFlush(toCreate));
    }

    public ReservationDTO findReservation(Long reservationId) {
        log.info("[RESERVATION-SERVICE] - Looking for reservation id {}.", reservationId);
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        log.info("[RESERVATION-SERVICE] Reservation id {} was successfully canceled.", reservationId);
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            this.validateCancellation(reservation);
            BigDecimal refundValue = getRefundValue(reservation);
            log.info("[RESERVATION-SERVICE] - Calculating refund and updating...");
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        log.info("[RESERVATION-SERVICE] - Updating...");
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        log.info("[RESERVATION-SERVICE] - Calculating refund...");
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours > 0 && hours <= 2) {
            return reservation.getValue().multiply(BigDecimal.valueOf(3 / 4));
        }
        if (hours > 2 && hours < 12) {
            return reservation.getValue().multiply(BigDecimal.valueOf(1 / 2));
        }
        if (hours >= 12 && hours < 24) {
            return reservation.getValue().multiply(BigDecimal.valueOf(1 / 4));
        }
        if (hours >= 24) {
            return reservation.getValue();
        }
        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);
        var currentSchedule = scheduleService.findSchedule(scheduleId);

        log.info("[RESERVATION-SERVICE] - Checking data...");
        if (currentSchedule.getStartDateTime().equals(previousReservation.getSchedule().getStartDateTime())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        BigDecimal refundValue = getRefundValue(previousReservation);
        this.updateReservation(previousReservation, refundValue, ReservationStatus.RESCHEDULED);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));

        log.info("[RESERVATION-SERVICE] - Rescheduled!");
        return newReservation;
    }
}
