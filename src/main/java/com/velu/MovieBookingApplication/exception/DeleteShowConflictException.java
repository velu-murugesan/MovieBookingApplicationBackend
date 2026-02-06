package com.velu.MovieBookingApplication.exception;

public class DeleteShowConflictException extends RuntimeException{
    public DeleteShowConflictException() {
    }

    public DeleteShowConflictException(String message) {
        super(message);
    }

    public DeleteShowConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteShowConflictException(Throwable cause) {
        super(cause);
    }

    public DeleteShowConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
