package com.tenniscourts.reservations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationDTO {

    private Long id;

    private String reservationStatus;

    private ReservationDTO previousReservation;

    private BigDecimal refundValue;

    private BigDecimal value;

    @NotNull
    private Long scheduledId;

    @NotNull
    private Long guestId;
}
