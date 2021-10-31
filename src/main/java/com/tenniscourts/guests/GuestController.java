package com.tenniscourts.guests;

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
import java.util.List;

@Api(tags = {"guests"},
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
@RequestMapping("/admin/guests")
public class GuestController extends BaseRestController{

    private final GuestService guestService;

    @ApiOperation(value = "Create guest")
    @PostMapping
    public ResponseEntity<Void> createGuest(@RequestBody GuestDTO createGuestRequestDTO){
        return ResponseEntity.created(locationByEntity(guestService.createGuest(createGuestRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Find all guests")
    @GetMapping
    public ResponseEntity<List<GuestDTO>> findAllGuests(){
        return ResponseEntity.ok(guestService.findAll());
    }

    @ApiOperation(value = "Find guest by id")
    @GetMapping("/{id}")
    public ResponseEntity<GuestDTO> findGuestById(@ApiParam (value = "Guest id")
                                                 @PathVariable("id") @NotEmpty @NotBlank Long guestId){
        return ResponseEntity.ok(guestService.findById(guestId));
    }

    @ApiOperation(value = "Find guest by id")
    @GetMapping("/{name}")
    public ResponseEntity<GuestDTO> findGuestByName(@ApiParam (value = "Guest name")
                                           @PathVariable("name") @NotEmpty @NotBlank String name){
        return ResponseEntity.ok(guestService.findByName(name));
    }

    @ApiOperation(value = "Update guest")
    @PutMapping
    public ResponseEntity<GuestDTO> updateGuest(@RequestBody GuestDTO updateGuestRequestDTO){
        return ResponseEntity.ok().body(guestService.updateGuest(updateGuestRequestDTO));
    }

    @ApiOperation(value = "Delete guest")
    @DeleteMapping
    public ResponseEntity<Void> deleteGuest(@ApiParam (value = "Guest id")
                                     @PathVariable("id") @NotEmpty @NotBlank Long id){
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }

}
