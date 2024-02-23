package com.conference.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
  
 * @author Nagarjuna Paritala
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONFERENCE_ROOM_DETAILS")
public class ConferenceRoomDetailsEntity {

	@Id
	@Column(name = "ID")
	private Long conferenceRoomId;
	private String name;
	@Column(name = "CAPACITY")
	private int capacity;
	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;
	@Column(name = "UPDATED_DATE")
	private LocalDateTime updatedDate;

	@PrePersist
	protected void onCreate() {
		createdDate = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = LocalDateTime.now();
	}
}
