package com.conference.service.impl;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.conference.service.ConfRoomDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.ConfRoomDetails;
import com.conference.entity.ConferenceRoomDetailsEntity;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataTransformer;
import com.conference.repository.ConfRoomDetailsRepository;

/**
 * @author Nagarjuna Paritala
 */
@Service
public class ConfRoomDetailsServiceImpl implements ConfRoomDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(ConfRoomDetailsServiceImpl.class);
	
    @Autowired
    private ConfRoomDetailsRepository conferenceRoomRepo;
    @Autowired
    private ConfRoomBookingServiceImpl bookingServiceImpl;
    @Autowired
    private DataTransformer dataTransformer;
    @Autowired
	private UtilsService utilsService;


	/**
	 * Fetch available rooms for requested slots if available
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<ConfRoomDetails> getAvailableRooms(LocalTime startTime, LocalTime endTime) {
		logger.info("Finding available conf rooms for the slots startTime: {} and endTime: {} ",startTime,endTime);
		validateBookingForCurrentDate(startTime);
		checkMaintenanceSchedule(startTime,endTime);
		List<ConferenceRoomDetailsEntity> allRooms = conferenceRoomRepo.findAll();
		List<ConferenceRoomDetailsEntity> bookedRooms = allRooms.stream()
				.filter(room -> bookingServiceImpl.isRoomAvailable(room.getConferenceRoomId(), startTime, endTime))
				.toList();
		logger.info("rooms booked for this slot :{} ",bookedRooms.size());
		allRooms.removeAll(bookedRooms);
		return dataTransformer.transEntityToConfRoomDetails(allRooms);
	}
	
	/**
	 * Validates and returns if requested slot is future date or not
	 * @param timeSlot
	 */
	private void validateBookingForCurrentDate(LocalTime timeSlot) {
		logger.info("Validating booking slot is for future date");
		bookingServiceImpl.validateRequestedBookingForCurrentDate(timeSlot);
	}

	/**
	 * This method will find conference room by id
	 * @param roomId
	 * @return
	 */
	@Override
	public ConferenceRoomDetailsEntity fetchConfRoomById(Long roomId) {
		logger.info("fetching conference room details by ID {}.......",roomId);
        return conferenceRoomRepo.findById(roomId).orElse(null);
    }
	
	/**
	 * Validate requested timings are conf room scheduled maintenance timings
	 * @param confStartTime
	 * @param confEndTime
	 */
	private void checkMaintenanceSchedule(LocalTime confStartTime, LocalTime confEndTime) {
		logger.info("Validating the conf room maintenance window timings");
		if (utilsService.isMaintenanceScheduled(confStartTime, confEndTime)) {
            throw new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage());
        }
	}
}