package com.conference.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nagarjuna Paritala
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String organizer;
	@ManyToOne
    @JoinColumn(name = "conferenceRoomId")
	private ConferenceRoomDetailsEntity confRoomDetails;
	private String status;
	private int participants;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalDateTime createdDate;
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
