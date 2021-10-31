package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        //TODO: implement addSchedule
        var tennisCourt = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId());
        log.info("[SCHEDULE-SERVICE] - Adding schedule...");
        return scheduleMapper.map(tennisCourt.map(court -> scheduleRepository.saveAndFlush(Schedule.builder()
                .tennisCourt(court)
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L))
                .build())).orElseThrow(() -> {
            throw new AlreadyExistsEntityException("Schedule already exists.");
        }));
    }

    @Query("SELECT * FROM schedule i where i.start_date_time = :startDate AND i.end_date_time = :endDate")
    public List<ScheduleDTO> findSchedulesByDates(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate) {
        //TODO: implement
        var list = scheduleRepository.findByDates(startDate, endDate);
        List<ScheduleDTO> listDto = new ArrayList<>();

        list.forEach(schedule -> listDto.add(
                ScheduleDTO.builder()
                        .id(schedule.getId())
                        .startDateTime(schedule.getStartDateTime())
                        .endDateTime(schedule.getEndDateTime())
                        .tennisCourtId(schedule.getTennisCourt().getId())
                        .build()
        ));
        return listDto;
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        //TODO: implement
        log.info("[SCHEDULE-SERVICE] - Looking for schedule id {} ", scheduleId);
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        log.info("[SCHEDULE-SERVICE] - Looking for schedule for tennis court id {} ", tennisCourtId);
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
