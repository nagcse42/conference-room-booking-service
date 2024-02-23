package com.conference.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.conference.dto.BookingResponse;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.service.impl.ConfRoomBookingServiceImpl;

/**
 * @author Nagarjuna Paritala
 */
@WebMvcTest(controllers = { BookingController.class })
public class BookingControllerTest {

    @Autowired	
    private MockMvc mockMvc;

    @MockBean
    private ConfRoomBookingServiceImpl bookingService;
    
	/**
	 * Booking conference room with valid(18) participants.
	 */
	@Test
    void bookConferenceRoom_ValidBooking_ReturnsBookedRoom() throws Exception {
		BookingResponse newBooking = mockBookingResponse();
        lenient().when(bookingService.bookConferenceRoom(any(),anyString())).thenReturn(newBooking);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"16:00:00\", \"endTime\": \"17:00:00\", \"participantsCount\": 18 }")
        		.header("user", "Nagarjunap"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.confRoomName").value("Strive"))
                .andExpect(jsonPath("$.data.confRoomCapacity").value(20))
                .andExpect(jsonPath("$.data.startTime").value("16:00:00"))
                .andExpect(jsonPath("$.data.endTime").value("17:00:00"))
                .andExpect(jsonPath("$.data.participants").value(18))
                .andExpect(jsonPath("$.data.status").value("Booked"));
    }
	
	/**
	 * Test case    : Book conference room with 5 participants/ No Rooms free to book  
	 * Excepted Results : Return 400 Bad Request with error message as No room available or exceeding room capacity.
	 * 
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_NoRoom_Available_Or_ExceedingCapacity_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"14:00:00\"	, \"endTime\": \"15:00:00\", \"participantsCount\": 5 }")
                .header("user", "Nagarjunap"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.status").value("error"))
		        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.ALL_ROOMS_BOOKED.name()))
		        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
    }
    
    /**
	 * Book conference room with invalid(1) participants
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_With_InvalidNumberOfParticipants_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.LESS_PARTICIPANTS.name(),ErrorCodes.LESS_PARTICIPANTS.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"14:00:00\"	, \"endTime\": \"15:00:00\", \"participantsCount\": 5 }")
                .header("user", "Nagarjunap"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.status").value("error"))
		        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.LESS_PARTICIPANTS.name()))
		        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.LESS_PARTICIPANTS.getErrorMessage()));
    }
    
    /**
	 * Book conference room when rooms between maintains period(9 am - 10 am)
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_with_MaintainsPeriodTimings_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"09:00:00\"	, \"endTime\": \"10:00:00\", \"participantsCount\": 5 }")
                .header("user", "Nagarjunap"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.status").value("error"))
		        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.UNDER_MAINTENANCE_EXC.name()))
		        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
    }
    
    /**
   	 * Book conference room with invalid timeslots
   	 * @throws Exception
   	 */
       @Test
       void bookConferenceRoom_InvalidTime_Slots_ThrowsException() throws Exception {
    	   when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name(),ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage()));
           mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{ \"startTime\": \"09:00:00\"	, \"endTime\": \"10:00:00\", \"participantsCount\": 5 }")
                   .header("user", "Nagarjunap"))
			       .andExpect(status().isOk())
			       .andExpect(jsonPath("$.status").value("error"))
			       .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name()))
			       .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage()));
       }
    
    /**
     * book conference room with invalid intervals(10 mints)
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_With_InvalidTime_Intervals_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name(),ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"10:10:00\", \"endTime\": \"10:30:00\", \"participantsCount\": 2 }")
                .header("user", "Nagarjunap"))
	       		.andExpect(status().isOk())
	       		.andExpect(jsonPath("$.status").value("error"))
	       		.andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name()))
	       		.andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage()));
    }
    /**
     * Try with null time slots to book conference room
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_With_NullTimeSlots_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"participantsCount\": 5 }")
                .header("user", "Nagarjunap"))
                .andExpect(status().isBadRequest());               
    }
    
    /**
     * Test Scenario    : Try without participants to book conference room ( Validation failed )
     * Excepted Results : Return 400 Bad Request
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_Without_Participants_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"14:00:00\" , \"endTime\": \"15:00:00\" }")
                .header("user", "Nagarjuna Paritala"))
                .andExpect(status().isBadRequest());
               
    }

    private BookingResponse mockBookingResponse() {
    	BookingResponse bookingResponse = new BookingResponse();
    	bookingResponse.setStartTime(LocalTime.parse("16:00:00"));
    	bookingResponse.setEndTime(LocalTime.parse("17:00:00"));
    	bookingResponse.setConfRoomCapacity(20);
    	bookingResponse.setStatus("Booked");
    	bookingResponse.setConfRoomName("Strive");
    	bookingResponse.setParticipants(18);
        return bookingResponse;
    }
}