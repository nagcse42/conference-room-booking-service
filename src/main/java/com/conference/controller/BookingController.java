package com.conference.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.exception.RoomBookingException;
import com.conference.service.ConfRoomBookingService;
import com.conference.constants.ConferenceConstants;
import com.conference.dto.Response;

/**
  
 * @author Nagarjuna Paritala
 */

@RestController
@RequestMapping("/api/v1/bookings/")
@Validated
public class BookingController {

	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private ConfRoomBookingService confRoomBookingService;


    /**
     * API for booking conference room
     * @param bookingDetails
     * @param user
     * @return
     * @throws RoomBookingException
     */
    @PostMapping("bookConferenceRoom")
	public ResponseEntity<Response<BookingResponse>> bookConferenceRoom(@RequestBody @Valid BookingDetails bookingDetails,
			@RequestHeader(value = ConferenceConstants.LOGGED_IN_USER, required = false) String user) throws RoomBookingException {
    	logger.info("Booking request accepted for the slot {} - {} ",bookingDetails.getStartTime().toString(),bookingDetails.getEndTime().toString());
    	 BookingResponse bookingResponse = confRoomBookingService.bookConferenceRoom(bookingDetails,user);
        Response<BookingResponse> response = Response.<BookingResponse>builder()
    			.status(ConferenceConstants.SUCCESS)
    			.data(bookingResponse)
    			.build();          
        logger.info("Conference room booking done successfully for the room: {}", bookingResponse.getConfRoomName());
      return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}