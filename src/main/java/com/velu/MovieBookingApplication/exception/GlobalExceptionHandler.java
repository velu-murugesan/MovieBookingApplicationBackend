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
    public ResponseEntity<ErrorResponse> ourException(
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



    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> ourException(
           UserAlreadyExistsException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "USER_ALREADY_EXISTS",
                exception.getMessage(),
                request.getRequestURI()
        );


        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DeleteShowConflictException.class)
    public ResponseEntity<ErrorResponse> ourException(
            DeleteShowConflictException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT WHILE DELETING SHOW",
                exception.getMessage(),
                request.getRequestURI()
        );


        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> ourException(
            RateLimitExceededException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Request",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.TOO_MANY_REQUESTS);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> ourException(
            BadRequestException exception,
            HttpServletRequest request
    ){


        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid user request , please check you inputs",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SeatsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> ourException(
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
    public ResponseEntity<ErrorResponse> ourException(
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
    public ResponseEntity<ErrorResponse> ourException(
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



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> ourException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "RESOURCE_IS_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBookingCancellationException.class)
    public ResponseEntity<ErrorResponse> ourException(
            InvalidBookingCancellationException exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "BOOKING_CAN'T_BE_CANCEL",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> ourException(
           Exception exception,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error , Please wait for a minute",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
