package com.conference.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.dto.ConfRoomDetails;
import com.conference.entity.BookingDetailsEntity;
import com.conference.entity.ConferenceRoomDetailsEntity;
import com.conference.constants.ConferenceConstants;

/**
 * @author Nagarjuna Paritala
 */

@Component
public class DataTransformer {

	/**	
	 * Transforms entity to DTO.
	 * @param allRooms
	 * @return
	 */
	public List<ConfRoomDetails> transEntityToConfRoomDetails(List<ConferenceRoomDetailsEntity> allRooms) {
		return allRooms.stream().map(room -> new ConfRoomDetails(room.getConferenceRoomId(), room.getName(), room.getCapacity()))
				.collect(Collectors.toList());
	}


	/**
	 * Transforms booking details to entity
	 * @param bookingDetails
	 * @param user
	 * @param availableRoom
	 * @return
	 */
	public BookingDetailsEntity transformBookingDetailsToEntity(BookingDetails bookingDetails, String user, ConferenceRoomDetailsEntity availableRoom) {
		  BookingDetailsEntity bookingDetailsEntity = new BookingDetailsEntity();
		  bookingDetailsEntity.setStartTime(bookingDetails.getStartTime());
		  bookingDetailsEntity.setEndTime(bookingDetails.getEndTime());
		  bookingDetailsEntity.setOrganizer(user);
		  bookingDetailsEntity.setConfRoomDetails(availableRoom);
		  bookingDetailsEntity.setStatus(ConferenceConstants.BOOKED);
		  bookingDetailsEntity.setParticipants(bookingDetails.getParticipantsCount());
		  return bookingDetailsEntity;
		}


	/**
	 * Transforms booking data to response
 	 * @param confirmedBooking
	 * @return
	 */
	public BookingResponse transformsBookingDataToResponse(BookingDetailsEntity confirmedBooking) {
		BookingResponse bookingResponse = new BookingResponse();
		bookingResponse.setConfRoomCapacity(confirmedBooking.getConfRoomDetails().getCapacity());
		bookingResponse.setConfRoomName(confirmedBooking.getConfRoomDetails().getName());
		bookingResponse.setParticipants(confirmedBooking.getParticipants());
		bookingResponse.setStatus(confirmedBooking.getStatus());
		bookingResponse.setStartTime(confirmedBooking.getStartTime());
		bookingResponse.setEndTime(confirmedBooking.getEndTime());
		return bookingResponse;
	}
}
