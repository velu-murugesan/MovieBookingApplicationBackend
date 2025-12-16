package com.velu.MovieBookingApplication.exception;

public class InvalidBookingCancellationException extends RuntimeException{

    public InvalidBookingCancellationException(String message) {
        super(message);
    }
}
