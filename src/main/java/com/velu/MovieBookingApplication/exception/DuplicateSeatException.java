package com.velu.MovieBookingApplication.exception;

public class DuplicateSeatException extends RuntimeException{

    public DuplicateSeatException(String message){
         super(message);
    }

}
