package com.conference.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.dto.ConfRoomDetails;
import com.conference.entity.BookingDetailsEntity;
import com.conference.entity.ConferenceRoomDetailsEntity;

@ExtendWith(MockitoExtension.class)
class DataTransformerTest {
	
	@InjectMocks
	private DataTransformer dataTransformer;

	@Test
	void testMapConferenceRoomDetails() {
		ConferenceRoomDetailsEntity conferenceRoomDetailsEntity = new ConferenceRoomDetailsEntity();
		conferenceRoomDetailsEntity.setConferenceRoomId(1L);
		conferenceRoomDetailsEntity.setName("Amaze");
		conferenceRoomDetailsEntity.setCapacity(10);
		List<ConfRoomDetails> confRoomDetailsList = dataTransformer.transEntityToConfRoomDetails(Collections.singletonList(conferenceRoomDetailsEntity));
		assertNotNull(confRoomDetailsList);
		assertEquals(1, confRoomDetailsList.size());
		assertEquals("Amaze", confRoomDetailsList.get(0).getName());
		assertEquals(1, confRoomDetailsList.get(0).getId());
		assertEquals(10, confRoomDetailsList.get(0).getCapacity());
	}

	@Test
	void testMapToBookingDataEntity() {
		String user = "TestUser";
		BookingDetails bookingDetails = new BookingDetails();		
		bookingDetails.setStartTime(LocalTime.of(10,00));
		bookingDetails.setEndTime(LocalTime.of(11,00));
		bookingDetails.setParticipantsCount(10);
		
		ConferenceRoomDetailsEntity conferenceRoom = new ConferenceRoomDetailsEntity();
		conferenceRoom.setConferenceRoomId(1l);
		conferenceRoom.setCapacity(20);
		conferenceRoom.setName("Amaze");
		conferenceRoom.setCreatedDate(LocalDateTime.now());
		
		BookingDetailsEntity bookingDetailsEntity = dataTransformer.transformBookingDetailsToEntity(bookingDetails, user, conferenceRoom);
		assertEquals(10, bookingDetailsEntity.getParticipants());
		assertEquals("Amaze", bookingDetailsEntity.getConfRoomDetails().getName());
		assertEquals(10, bookingDetailsEntity.getParticipants());
		assertEquals(10, bookingDetailsEntity.getStartTime().getHour());
		assertEquals(11, bookingDetailsEntity.getEndTime().getHour());
	}

	@Test
	void testMapBookingDataToBookingResponse() {
		ConferenceRoomDetailsEntity conferenceRoom = new ConferenceRoomDetailsEntity();
		conferenceRoom.setConferenceRoomId(1l);
		conferenceRoom.setCapacity(20);
		conferenceRoom.setName("Amaze");
		conferenceRoom.setCreatedDate(LocalDateTime.now());
		
		BookingDetailsEntity confirmedBookingInfo = new BookingDetailsEntity();
		confirmedBookingInfo.setCreatedDate(LocalDateTime.now());
		confirmedBookingInfo.setStartTime(LocalTime.of(10, 00));
		confirmedBookingInfo.setEndTime(LocalTime.of(11,00));
		confirmedBookingInfo.setParticipants(10);
		confirmedBookingInfo.setStatus("Booked");
		confirmedBookingInfo.setConfRoomDetails(conferenceRoom);
		
		BookingResponse bookingResponse = dataTransformer.transformsBookingDataToResponse(confirmedBookingInfo);
		assertEquals(20, bookingResponse.getConfRoomCapacity());
		assertEquals("Amaze", bookingResponse.getConfRoomName());
		assertEquals(10, bookingResponse.getParticipants());
		assertEquals("Booked", bookingResponse.getStatus());
	}
}
