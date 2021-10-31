package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Api(tags = {"schedule"},
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    //TODO: implement rest and swagger
    @ApiOperation(value = "Add schedule tennis court")
    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    //TODO: implement rest and swagger
    @ApiOperation(value = "Find schedules by dates")
    @GetMapping("/date")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@RequestParam("localStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam("localEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    //TODO: implement rest and swagger
    @ApiOperation(value = "Find schedules by id")
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@ApiParam (value = "Schedule id")
                                                            @PathVariable("id") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
