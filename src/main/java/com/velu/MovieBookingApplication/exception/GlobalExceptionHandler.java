package com.velu.MovieBookingApplication.exception;
import com.velu.MovieBookingApplication.dtoresponse.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Message not Readable",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation Failed",
                request.getRequestURI()
        );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {

               ErrorResponse errorResponse = new ErrorResponse(
                       LocalDate.now(),
                       HttpStatus.BAD_REQUEST.value(),
                       HttpStatus.BAD_REQUEST.getReasonPhrase(),
                       "Method Argument type mismatch",
                       request.getRequestURI()
               );
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        return  new ResponseEntity<ErrorResponse>(
                new ErrorResponse(
                        LocalDate.now(),
                        ex.getStatusCode().value(),
                        ex.getStatusCode().toString(),
                        "Missing request parameter" + ex.getParameterName(),
                        request.getRequestURI()
                ),
                ex.getStatusCode()
        );
    }



    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleClientErrors(
            ResponseStatusException e,
            HttpServletRequest request
    ){
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDate.now(),
                        e.getStatusCode().value(),
                        e.getStatusCode().toString(),
                        e.getReason(),
                        request.getRequestURI()
                ),
                e.getStatusCode()
        );

    }


    public ResponseEntity<ErrorResponse> handleServerErrors(
            Exception e,
            HttpServletRequest request
    ){

        return  new ResponseEntity<>(
                new ErrorResponse(
                        LocalDate.now(),
                        500,
                        "Internal_Server_Error",
                        "Something went wrong",
                        request.getRequestURI()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle405(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDate.now(),
                        405,
                        "METHOD_NOT_ALLOWED",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

}
