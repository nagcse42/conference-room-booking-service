package com.conference.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nagarjuna Paritala
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetails {
	@NotNull(message = "startTime missing")
    private LocalTime startTime;
	@NotNull(message = "endTime missing")
    private LocalTime endTime;
	@NotNull(message = "participantsCount missing")
    private Integer participantsCount;
}
