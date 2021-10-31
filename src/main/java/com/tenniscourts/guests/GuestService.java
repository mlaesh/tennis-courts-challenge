package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO createGuest(GuestDTO createGuestDTO){
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(createGuestDTO)));
    }

    public List<GuestDTO> findAll(){
        List<GuestDTO> guestDTOList = new ArrayList<>();
        var guests = guestRepository.findAll();

        guests.forEach(guest -> guestDTOList.add(
                GuestDTO.builder()
                .id(guest.getId())
                .name(guest.getName())
                .build()));
        return guestDTOList;
    }

    public GuestDTO findById(Long guestId) {
        return guestMapper.map(guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found.")));
    }

    public GuestDTO findByName(String name){
        try {
            return guestMapper.map(guestRepository.findByName(name));
        } catch (Exception exception){
            throw new EntityNotFoundException("Guest not found.");
        }
    }

    public GuestDTO updateGuest(GuestDTO updateGuest){
        var toUpdate = guestRepository.findById(updateGuest.getId())
                .map(guestMapper::map)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Guest not found.");
                });
        toUpdate.setName(updateGuest.getName());
        return guestMapper.map(guestRepository.save(guestMapper.map(toUpdate)));
    }

    public void deleteGuest(Long guestId){
        guestRepository.delete(guestRepository.findById(guestId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Guest not found.");
                }));
    }
}
