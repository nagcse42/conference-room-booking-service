package com.conference.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.conference.dto.ConfRoomDetails;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.service.impl.ConfRoomDetailsServiceImpl;


/**
 * @author Nagarjuna Paritala
 */
@WebMvcTest(controllers = { ConferenceRoomController.class})
class ConferenceRoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConfRoomDetailsServiceImpl conferenceRoomService;
	
	/**
	 * Get available rooms for 10:00 to 11:00 time slot
	 */
	@Test
	void getAvailableRooms_ValidRequest_Returns_AvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(), any()))
				.thenReturn(Collections.singletonList(mockConfRoomDetails()));
		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "10:00")
				.param("endTime", "11:00").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1)) 
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Amaze"))
                .andExpect(jsonPath("$.data[0].capacity").value(2));
	}
	
	/**
	 * Get available rooms for maintenance period time
	 */
	@Test
	void getAvailableRooms_For_MaintenancePeriod_Should_Return_NoAvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(),any())).thenThrow(new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("error"))
        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.UNDER_MAINTENANCE_EXC.name()))
        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
	}

	@Test
	void getAvailableRooms_NoAvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(),any())).thenThrow(new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("error"))
        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.ALL_ROOMS_BOOKED.name()))
        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
	}

	private ConfRoomDetails mockConfRoomDetails() {
		ConfRoomDetails room = new ConfRoomDetails();
		room.setId(1L);
		room.setName("Amaze");
		room.setCapacity(2);
		return room;
	}
}
