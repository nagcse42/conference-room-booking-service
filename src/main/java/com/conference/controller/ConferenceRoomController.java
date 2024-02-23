package com.conference.controller;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conference.dto.ConfRoomDetails;
import com.conference.service.ConfRoomDetailsService;
import com.conference.constants.ConferenceConstants;
import com.conference.dto.Response;

/**
 * @author Nagarjuna Paritala
 */

@RestController	
@RequestMapping("/api/v1/conference-rooms")
@Validated
public class ConferenceRoomController {
	
	private static final String END_TIME = "endTime";

	private static final String HH_MM = "HH:mm";

	private static final String START_TIME = "startTime";

	private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomController.class);

    @Autowired
    private ConfRoomDetailsService confRoomDetailsService;

    /**
     * User can be able to see meeting rooms available by giving the time range
     * 
     * @param startTime : meeting start time
     * @param endTime : meeting end time
     * @return : list of available conferenceRooms 
     */
    @GetMapping
	public ResponseEntity<Response<List<ConfRoomDetails>>> getAvailableConferenceRooms(
			@RequestParam(START_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime startTime,
            @RequestParam(END_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime endTime) {
    	logger.info("Started getAvailableRooms for the time slot {} -{}",startTime,endTime);
    	Response<List<ConfRoomDetails>> response = Response.<List<ConfRoomDetails>>builder()
    			.status(ConferenceConstants.SUCCESS)
    			.data(confRoomDetailsService.getAvailableRooms(startTime, endTime))
    			.build();          
      return  ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
