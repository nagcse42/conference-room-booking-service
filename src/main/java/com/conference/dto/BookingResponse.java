package com.conference.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nagarjuna Paritala
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
	private String confRoomName;
	private int confRoomCapacity;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer participants;
    private String status;
}
