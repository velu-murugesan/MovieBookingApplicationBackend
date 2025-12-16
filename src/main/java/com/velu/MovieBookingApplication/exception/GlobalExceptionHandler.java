package com.velu.MovieBookingApplication.exception;
import com.velu.MovieBookingApplication.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSeatSelectionException.class)
    public ResponseEntity<ErrorResponse> customException(
            InvalidSeatSelectionException exception,
            HttpServletRequest request
    ){


      ErrorResponse error = new ErrorResponse(
              LocalDate.now(),
              HttpStatus.BAD_REQUEST.value(),
              "INVALID_SEAT_SELECTION",
              exception.getMessage(),
              request.getRequestURI()
      );

      return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SeatsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> customException(
            SeatsNotAvailableException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "SEATS_UNAVAILABLE",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateSeatException.class)
    public ResponseEntity<ErrorResponse> customException(
            DuplicateSeatException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "SEATS_ALREADY_BOOKED",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ErrorResponse> customException(
            InvalidBookingStateException exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "INVALID_BOOKING_STATE",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(
            CustomException exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "REQUEST_IS_NOT_SATISFIED",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBookingCancellationException.class)
    public ResponseEntity<ErrorResponse> customException(
            InvalidBookingCancellationException exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONTINUE.value(),
                "BOOKING_CAN'T_BE_CANCEL",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

}
