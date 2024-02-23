package com.conference.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.BookingDetailsEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Nagarjuna Paritala
 */
@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetailsEntity, Long> {
    List<BookingDetailsEntity> findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(Long roomId, LocalTime start, LocalTime end);
}

