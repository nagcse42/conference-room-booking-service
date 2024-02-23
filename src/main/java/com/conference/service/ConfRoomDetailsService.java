package com.conference.service;

import java.time.LocalTime;
import java.util.List;

import com.conference.dto.ConfRoomDetails;
import com.conference.entity.ConferenceRoomDetailsEntity;

/**
 * @author Nagarjuna Paritala
 */
public interface ConfRoomDetailsService {
	List<ConfRoomDetails> getAvailableRooms(LocalTime startTime, LocalTime endTime);
	ConferenceRoomDetailsEntity fetchConfRoomById(Long roomId);
}
