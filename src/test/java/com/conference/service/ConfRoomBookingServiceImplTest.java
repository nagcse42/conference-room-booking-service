package com.conference.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Collections;

import com.conference.service.impl.ConfRoomBookingServiceImpl;
import com.conference.service.impl.ConfRoomDetailsServiceImpl;
import com.conference.service.impl.UtilsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.entity.BookingDetailsEntity;
import com.conference.entity.ConferenceRoomDetailsEntity;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataTransformer;
import com.conference.repository.BookingDetailsRepository;
import com.conference.repository.ConfRoomDetailsRepository;

/**
 * @author Nagarjuna Paritala
 */
@ExtendWith(MockitoExtension.class)
class ConfRoomBookingServiceImplTest {

	private static final String LOGGED_IN_USER = "user";

	@Mock
    private BookingDetailsRepository bookingDetailsRepository;
	
	@Mock
    private ConfRoomDetailsRepository confRoomDetailsRepository;

    @Mock
    private ConfRoomDetailsServiceImpl conferenceRoomService;

    @Mock
    private UtilsService utilsService;
    
    @Mock
    private DataTransformer dataTransformer;

    @InjectMocks
    private ConfRoomBookingServiceImpl bookingService;
    
    /**
     * Test case : User book conference room successfully 
     * 
     * 
     */
    @Test
   void bookRoom_ValidBooking_ReturnsBooking() {
        BookingDetails newBooking = createValidBooking();
        when(utilsService.isMaintenanceScheduled(any(), any())).thenReturn(false);
        when(dataTransformer.transformBookingDetailsToEntity(any(),anyString(),any())).thenReturn(new BookingDetailsEntity());
        when(dataTransformer.transformsBookingDataToResponse(any())).thenReturn(new BookingResponse());
        when(confRoomDetailsRepository.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(anyInt())).thenReturn(Collections.singletonList(createValidConferenceRoom()));
        when(bookingDetailsRepository.save(any())).thenReturn(createBookingData());
        BookingResponse bookedRoom = bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER);
        assertNotNull(bookedRoom);
    }
    
    /**
     * Test case : User unable to book conference room due to no availability.
     */
    @Test
    void bookRoom_NoRooms_Available_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    /**
     * Test case : User unable to book conference room due to participants are not more than 1
     */
    @Test
    void bookRoom_For_Single_Participants_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setParticipantsCount(1);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    
    /**
     * Test case : User unable to book conference room due to participants are more than what we have capacity try with 55
     * 
     */
    @Test
   void bookRoom_For_55_Participants_ThrowsException() {
        BookingDetails newBooking = createValidBooking();
        newBooking.setParticipantsCount(55);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    
    /**
     * Test case : User unable to book conference room due to Invalid start and end time(End is prior to start time)
     */
    @Test
    void bookRoom_Invalid_BookData_EndTime_Prior_StartTime_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setStartTime(LocalTime.of(10,00));
    	newBooking.setEndTime(LocalTime.of(9,00));
    	
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    /**
     * Test case : User Unable to book the conference room due to the schedule is under maintenance scheduled (17-18hr) 
     */
   @Test
   void bookRoom_MaintenanceScheduled_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setStartTime(LocalTime.of(17,00));
    	newBooking.setEndTime(LocalTime.of(18,00));
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
   
   /**
    * Test case : User Unable to book the conference room due to the schedule is pasttime (7-8hr) 
    */
  @Test
  void bookRoom_PastTime_ThrowsException() {
   	BookingDetails newBooking = createValidBooking();
   	newBooking.setStartTime(LocalTime.of(LocalTime.now().getHour()-1,00));
   	newBooking.setEndTime(LocalTime.of(LocalTime.now().getHour(),00));
       assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
   }
   
   /**
    * Test case : User Unable to book the conference room due to the invalid Intervals (10-10:10hr) 
    */
   @Test
   void bookRoom_Invalid_15m_Intervals_ThrowsException() {
   	BookingDetails newBooking = createValidBooking();
   	newBooking.setStartTime(LocalTime.of(10,00));
   	newBooking.setEndTime(LocalTime.of(10,10));
   	
   assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
   }
   
   /**
    * Test case : Verify the Room no 1 is booked when rooms available then return room details
    *  
    */
   @Test
   void testIsRoomBooked_When_Room_Available_Then_Return_False() {
	   when(bookingDetailsRepository.findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(any(),any(),any())).thenReturn(Collections.singletonList(new BookingDetailsEntity()));
	   assertTrue(bookingService.isRoomAvailable(1l,LocalTime.now(),LocalTime.of(LocalTime.now().getHour()+1,30)));
   }

   /**
    * Test case : Verify the Room no 1 is booked when room already booked then return empty list 
    *  
    */
   @Test
   void testIsRoomBooked_When_Room_Available_Then_Return_True() {
	   when(bookingDetailsRepository.findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(any(),any(),any())).thenReturn(Collections.emptyList());
	   assertFalse(bookingService.isRoomAvailable(1l,LocalTime.now(),LocalTime.of(LocalTime.now().getHour()+1,30)));
   }



    private BookingDetailsEntity createBookingData() {
    	BookingDetailsEntity bookingDetailsEntity = new BookingDetailsEntity();
    	bookingDetailsEntity.setId(1l);
    	bookingDetailsEntity.setParticipants(3);
    	bookingDetailsEntity.setStartTime(LocalTime.of(LocalTime.now().getHour(),LocalTime.now().getMinute()+10));
    	bookingDetailsEntity.setEndTime(LocalTime.of(LocalTime.now().getHour(),LocalTime.now().getMinute()+25));
        return bookingDetailsEntity;
    }
    
    private BookingDetails createValidBooking() {
    	BookingDetails bookingData = new BookingDetails();
    	bookingData.setParticipantsCount(3);
    	bookingData.setStartTime(LocalTime.of(11,00));
    	bookingData.setEndTime(LocalTime.of(12,00));
        return bookingData;
    }
    
    private ConferenceRoomDetailsEntity createValidConferenceRoom() {
        ConferenceRoomDetailsEntity room = new ConferenceRoomDetailsEntity();
        room.setConferenceRoomId(1L);
        room.setCapacity(10);
        return room;
    }
    
}
