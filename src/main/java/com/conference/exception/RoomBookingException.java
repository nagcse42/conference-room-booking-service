package com.conference.exception;

import lombok.Data;

/**
 * @author Nagarjuna Paritala
 */
@Data
public class RoomBookingException extends RuntimeException {

	private static final long serialVersionUID = 7514408454818505244L;
    private String errorCode;
    private String message;

    public RoomBookingException(Throwable e) {
        super(e);
        this.errorCode = null;
        this.message = e.getMessage();
    }

    public RoomBookingException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }
}
