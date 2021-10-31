package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {

    @InjectMocks
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(guestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGuest()));
        when(guestService.findById(anyLong())).thenReturn(mockGuestDTO());
    }

    @Test
    public void should_create_guest(){
        when(guestService.createGuest(mockGuestDTO())).thenReturn(mockGuestDTO());
        var guestDTO = guestService.createGuest(mockGuestDTO());

        Assert.assertEquals(guestDTO.getId(), mockGuestDTO().getId());
        Assert.assertEquals(guestDTO.getName(), mockGuestDTO().getName());
    }

    @Test
    public void should_list_all_guests(){
        when(guestRepository.findAll()).thenReturn(mockGuestList());
        var guestList = guestService.findAll();

        Assert.assertTrue(guestList.size() > BigInteger.ZERO.intValue());
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_throws_exception_when_guestID_not_found(){
        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());
        guestService.findById(anyLong());
    }

    @Test
    public void must_return_Guest_by_id(){
        when(guestRepository.findById(mockGuestDTO().getId())).thenReturn(Optional.of(mockGuest()));
        when(guestService.findById(mockGuestDTO().getId())).thenReturn(mockGuestDTO());
        var guestDTO = guestService.findById(mockGuestDTO().getId());

        Assert.assertEquals(mockGuestDTO().getId(), guestDTO.getId());
    }

    @Test
    public void must_return_Guest_by_name(){
        when(guestRepository.findByName(mockGuest().getName())).thenReturn(mockGuest());
        when(guestService.findByName(mockGuest().getName())).thenReturn(mockGuestDTO());
        var guest = guestService.findByName(mockGuest().getName());

        Assert.assertEquals(mockGuest().getName(), guest.getName());
    }

    private GuestDTO mockGuestDTO() {
        return GuestDTO.builder().id(1L).name("Valentina").build();
    }

    private Guest mockGuest() {
        var guest = new Guest();
        guest.setName("Valentina");
        guest.setId(1L);
        return guest;
    }

    private List<Guest> mockGuestList() {
        List<Guest> guests = new ArrayList<>();
        guests.add(mockGuest());
        guests.add(mockGuest());
        return guests;
    }

}
