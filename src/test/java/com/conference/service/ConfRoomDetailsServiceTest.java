package com.conference.service;

import com.conference.dto.ConfRoomDetails;
import com.conference.entity.ConferenceRoomDetailsEntity;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataTransformer;
import com.conference.repository.ConfRoomDetailsRepository;
import com.conference.service.impl.ConfRoomBookingServiceImpl;
import com.conference.service.impl.ConfRoomDetailsServiceImpl;
import com.conference.service.impl.UtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


/**
 * @author Nagarjuna Paritala
 */
@ExtendWith(MockitoExtension.class)
class ConfRoomDetailsServiceTest {
    @InjectMocks
    private ConfRoomDetailsServiceImpl confRoomDetailsService;

    @Mock
    private ConfRoomDetailsRepository conferenceRoomRepo;

    @Mock
    private ConfRoomBookingServiceImpl bookingServiceImpl;

    @Mock
    private DataTransformer dataTransformer;

    @Mock
    private UtilsService utilsService;

    @Test
    void test_getAvailableRooms_checkMaintenanceSchedule(){
        LocalTime startTime = LocalTime.of(LocalTime.now().getHour(),0);
        LocalTime endTime = LocalTime.of(LocalTime.now().getHour()+1,0);
        List<ConfRoomDetails> confRoomDetails = confRoomDetailsService.getAvailableRooms(startTime, endTime);
        Assertions.assertNotNull(confRoomDetails);
    }

    @Test
    void test_getAvailableRooms_MaintenanceScheduledTimeSlot(){
        LocalTime startTime = LocalTime.of(LocalTime.now().getHour(),0);
        LocalTime endTime = LocalTime.of(LocalTime.now().getHour()+1,0);
        Mockito.when(utilsService.isMaintenanceScheduled(Mockito.isA(LocalTime.class),Mockito.isA(LocalTime.class)))
                .thenReturn(true);
        Assertions.assertThrows(RoomBookingException.class,
                ()->confRoomDetailsService.getAvailableRooms(startTime, endTime));
    }

    @Test
    void test_fetchConfRoomById() {
        Mockito.when(conferenceRoomRepo.findById(Mockito.isA(Long.class)))
                .thenReturn(Optional.of(new ConferenceRoomDetailsEntity()));
        ConferenceRoomDetailsEntity conferenceRoomDetails=confRoomDetailsService.fetchConfRoomById(1l);
        Assertions.assertNotNull(conferenceRoomDetails);
    }
}
