package com.conference.dto;

import lombok.Builder;
import lombok.Data;

/**
  
 * @author Nagarjuna Paritala
 */

@Data
@Builder
public class Response<T> {
	private String status;
	private T data;
}
