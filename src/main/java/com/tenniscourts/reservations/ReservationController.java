package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Api(tags = {"reservation"},
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "Create reservation")
    @PostMapping(value = {"user/reservation", "admin/reservation"})
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find reservation by id")
    @GetMapping("admin/reservation/{id}")
    public ResponseEntity<ReservationDTO> findReservation(@ApiParam (value = "Reservation id")
                                                              @PathVariable("id") @NotEmpty @NotBlank Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @ApiOperation(value = "Cancel reservation")
    @PutMapping(value = {"user/reservation/cancel", "admin/reservation/cancel"})
    public ResponseEntity<ReservationDTO> cancelReservation(@ApiParam (value = "Reservation id")
                                                                @NotEmpty @NotBlank Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @ApiOperation(value = "Reschedule reservation")
    @PutMapping(value = {"user/reservation/reschedule", "admin/reservation/reschedule"})
    public ResponseEntity<ReservationDTO> rescheduleReservation(@ApiParam(value = "Previous reservation id")
                                                                    @NotEmpty @NotBlank Long reservationId,
                                                                @RequestBody Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
