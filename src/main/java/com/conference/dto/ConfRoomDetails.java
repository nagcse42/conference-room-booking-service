package com.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nagarjuna Paritala
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfRoomDetails {
	private Long id;
	private String name;
	private int capacity;
}
