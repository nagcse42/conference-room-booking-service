package com.conference.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Nagarjuna Paritala
 */

@Getter
@AllArgsConstructor
public enum ErrorCodes {

	UNDER_MAINTENANCE_EXC("The rooms are under maintenance during the requested time slot."),

	INCORRECT_BOOKING_INTERVALS("Booking intervals must be in increments of 15 minutes."),

	ALL_ROOMS_BOOKED("No room available or exceeding room capacity."),

	BOOKING_FOR_FUTURE_SLOTS("Booking can only be done for the current date and future time slots."),

	LESS_PARTICIPANTS("Number of participants must be greater than 1."),

	EXCEEDING_CAPACITY("Exceeding room capacity.");

	private String errorMessage;
	 
}
