package com.conference.service.impl;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.config.ApplicationProperties;

/**
 * @author Nagarjuna Paritala
 */
@Service
public class UtilsService {
	@Autowired
	private ApplicationProperties applicationProperties;
	
    /**
     * This method checks if there is maintenance scheduled for the specified time slot.
     * 
     * @param startTime
     * @param endTime
     * @return
     */
	public boolean isMaintenanceScheduled(LocalTime startTime, LocalTime endTime) {
		return applicationProperties.getMaintenancePeriods().stream()
				.anyMatch(period -> startTime.isBefore(period.getEndTime()) && endTime.isAfter(period.getStartTime()));
	}

}