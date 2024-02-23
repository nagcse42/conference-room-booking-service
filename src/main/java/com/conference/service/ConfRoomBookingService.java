package com.conference.service;

import java.time.LocalTime;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;

/**
 * @author Nagarjuna Paritala
 */
public interface ConfRoomBookingService {
	BookingResponse bookConferenceRoom(BookingDetails bookingDetails, String user);
	boolean isRoomAvailable(Long roomId, LocalTime startTime, LocalTime endTime);
	void validateRequestedBookingForCurrentDate(LocalTime startTime);
}
