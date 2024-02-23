package com.conference.service.impl;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.conference.service.ConfRoomBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.entity.BookingDetailsEntity;
import com.conference.entity.ConferenceRoomDetailsEntity;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataTransformer;
import com.conference.repository.BookingDetailsRepository;
import com.conference.repository.ConfRoomDetailsRepository;
import com.conference.constants.ConferenceConstants;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Nagarjuna Paritala
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ConfRoomBookingServiceImpl implements ConfRoomBookingService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfRoomBookingServiceImpl.class);
	
	@Autowired
    private BookingDetailsRepository bookingDetailsRepository;
    
	@Autowired
    private ConfRoomDetailsRepository conferenceRoomRepo;
    
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private DataTransformer dataTransformer;



    /**
     * Validates and books the conference room
     * @param bookingDetails
     * @param user
     * @return
     */
	@Override
    public BookingResponse bookConferenceRoom(BookingDetails bookingDetails, String user) {
		logger.info("Booking conference room started for the user: {} and request: {}", user, bookingDetails);
    	validateBookingForCurrentDate(bookingDetails.getStartTime());
        validateBookingInterval(bookingDetails); 
        validateRoomCapacity(bookingDetails);  
        checkMaintenanceSchedule(bookingDetails); 
        ConferenceRoomDetailsEntity availableRoom = getAvailableConferenceRoom(bookingDetails);
        logger.info("Booking confirmed for the room: {}",availableRoom.getName());
        BookingDetailsEntity confirmedBooking =  bookingDetailsRepository.save(dataTransformer.transformBookingDetailsToEntity(bookingDetails,user,availableRoom));
		return dataTransformer.transformsBookingDataToResponse(confirmedBooking);
    }


    /**
     * Checks and returns if any room available for requested time slot
     * @param roomId
     * @param confStartTime
     * @param confEndTime
     * @return
     */
	@Override
    public boolean isRoomAvailable(Long roomId, LocalTime confStartTime, LocalTime confEndTime) {
		logger.info("Verifying the requested room available for booking");
        List<BookingDetailsEntity> overlappingBookings = bookingDetailsRepository
                .findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(roomId, confStartTime, confEndTime);
        return !overlappingBookings.isEmpty();
    }
    
    /**
     *  This method checks if there is maintenance scheduled for the specified time slot.
     *  
     * @param bookingDetails
     */
    private void checkMaintenanceSchedule(BookingDetails bookingDetails) {
        if (utilsService.isMaintenanceScheduled(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name() , ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage());
        }
    }
    
    /**
	 * The number of people allowed for booking should be greater than 1 and less
	 * than or equal to the maximum capacity of the conference room.
	 * 
	 * @param bookingDetails
	 */
	private void validateRoomCapacity(BookingDetails bookingDetails) {
		int participants = bookingDetails.getParticipantsCount();
		Optional.of(participants).filter(p -> p > 1)
				.orElseThrow(() -> new RoomBookingException(ErrorCodes.LESS_PARTICIPANTS.name(),(ErrorCodes.LESS_PARTICIPANTS.getErrorMessage())));
	}
    
    /**
     * @param confStartTime
     */
	@Override
    public void validateBookingForCurrentDate(LocalTime confStartTime) {
		logger.info("Validating the requested confStartTime: {} ",confStartTime);
    	Optional.ofNullable(confStartTime)
        .ifPresent(currentLocalTime -> {
            if (!currentLocalTime.isAfter(LocalTime.now())) {
                throw new RoomBookingException(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name(),ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage());
            }
        });
    }
    
    /**
     * The checkAvailability method verify for any overlapping bookings for the requested time slot. 
     * If there are overlapping bookings,it throws an RoomBookingException indicating that the room is already booked for the requested time.
     * New bookings are rejected if they conflict with existing bookings.
     * 
     * @param bookingDetails
     * @return 
     */
    private ConferenceRoomDetailsEntity getAvailableConferenceRoom(BookingDetails bookingDetails) {
    	logger.info("Booking is in process trying to find available rooms....");
    	List<ConferenceRoomDetailsEntity> allRooms = conferenceRoomRepo.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(bookingDetails.getParticipantsCount());
    	for (ConferenceRoomDetailsEntity conferenceRoomDetailsEntity : allRooms) {
    		 if(!isRoomAvailable(conferenceRoomDetailsEntity.getConferenceRoomId(), bookingDetails.getStartTime(), bookingDetails.getEndTime()))
    			 return conferenceRoomDetailsEntity;
    	}
        throw new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage());
    }
    
    /**
	 * Booking can be done only in intervals of 15 minutes. Examples : 2:00 - 2:15 or 2:00 - 2:30 or 2:00 - 3:00
	 * 
	 * @param bookingDetails
	 */
    private void validateBookingInterval(BookingDetails bookingDetails) {
    	logger.info("Booking is in process validating requested slots Intervals....");
        if (!isValidTimeInterval(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name(),ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage());
        }
    }

    /**
     * Validates the requested conference start and end time is under 15 mints intervals
     * @param confStartTime
     * @param confEndTime
     * @return
     */
    private boolean isValidTimeInterval(LocalTime confStartTime, LocalTime confEndTime) {
    	logger.info("Validating requested confStartTime: {} & confEndTime: {}", confStartTime, confEndTime);
        if (!isStartTimeBeforeEndTime(confStartTime, confEndTime) || !isValid15MinutesTimeSlot(confStartTime) || !isValid15MinutesTimeSlot(confEndTime)) {
            return false;
        }

        return true;
    }


    /**
     * Validates if confEndTime is after confStartTime
     * @param confStartTime
     * @param confEndTime
     * @return boolean
     */
    private boolean isStartTimeBeforeEndTime(LocalTime confStartTime, LocalTime confEndTime) {
    	logger.info("Booking is in process validating requested slots confStartTime should be prior to endTime....");
        return confStartTime.isBefore(confEndTime);
    }
    /**
     * Validates if timeslot is for 15 minutes window or not
     * @param timeSlot
     * @return boolean
     */
    private boolean isValid15MinutesTimeSlot(LocalTime timeSlot) {
    	logger.info("Validation started for 15 minutes time slot for: {}", timeSlot);
        return timeSlot.getMinute() % ConferenceConstants.MINITS_15 == ConferenceConstants.ZERO;
    }
}