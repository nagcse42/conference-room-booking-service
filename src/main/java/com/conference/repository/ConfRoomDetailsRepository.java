package com.conference.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.ConferenceRoomDetailsEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Nagarjuna Paritala
 */
@Repository
public interface ConfRoomDetailsRepository extends JpaRepository<ConferenceRoomDetailsEntity, Long> {
	List<ConferenceRoomDetailsEntity> findByCapacityGreaterThanEqualOrderByCapacityAsc(int participants);
}
