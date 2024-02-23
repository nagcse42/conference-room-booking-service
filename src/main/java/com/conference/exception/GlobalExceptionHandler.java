package com.conference.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.conference.constants.ConferenceConstants;
import com.conference.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nagarjuna Paritala
 */


@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RoomBookingException.class)
    public ResponseEntity<Response<ErrorResponse> > handleRoomBookingException(RoomBookingException ex) {
    	logger.error("Room booking exception occurred", ex);
    	Response<ErrorResponse> response = Response.<ErrorResponse>builder()
    			.status(ConferenceConstants.ERROR)
    			.data(new ErrorResponse(ex.getErrorCode(),ex.getMessage()))
    			.build();          

      return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public  ResponseEntity<Response<ErrorResponse>> handleRoomBookingException(Exception ex) {
    	logger.error("Exception occurred", ex);
    	Response<ErrorResponse> response = Response.<ErrorResponse>builder()
    			.status(ConferenceConstants.ERROR)
    			.data(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),ex.getMessage()))
    			.build();          
      return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @Data
    @AllArgsConstructor
    private class ErrorResponse {
        private String errorCode;
        private String errorDetails;

    }
}