package com.conference.config;

import java.time.LocalTime;

import lombok.Data;

/**
 * @author Nagarjuna Paritala
 */
@Data
public class MaintenancePeriodConfig {
	private LocalTime startTime;
	private LocalTime endTime;
}
