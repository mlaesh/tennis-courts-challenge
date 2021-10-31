package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"tennisCourt"},
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/court")
@AllArgsConstructor
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    //TODO: implement rest and swagger
    @ApiOperation(value = "Add tennis court")
    @PostMapping
    public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    //TODO: implement rest and swagger
    @ApiOperation(value = "Find court by id")
    @GetMapping("/{id}")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@ApiParam (value = "Tennis court id")
                                                                  @PathVariable("id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    //TODO: implement rest and swagger
    @ApiOperation(value = "Find court with schedules by id")
    @GetMapping("/schedules/{id}")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@ApiParam (value = "Tennis court id")
                                                                               @PathVariable("id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
