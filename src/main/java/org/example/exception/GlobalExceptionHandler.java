package org.example.exception;

import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> handleValidationException(Exception ex, WebRequest request) {

    String validationMessage = "Validation failed for DNA sequence.";

    if (ex instanceof ConstraintViolationException) {
      validationMessage = ex.getMessage();
    } else if (ex instanceof MethodArgumentNotValidException) {
      MethodArgumentNotValidException manvEx = (MethodArgumentNotValidException) ex;
      if (manvEx.getBindingResult().hasFieldErrors()) {
        validationMessage = manvEx.getBindingResult().getFieldError().getDefaultMessage();
      } else {
        validationMessage = "Invalid request body structure.";
      }
    }

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(validationMessage)
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(DnaHashCalculationException.class)
  public ResponseEntity<ErrorResponse> handleDnaHashCalculationException(DnaHashCalculationException ex, WebRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

    ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message("Internal server error: " + ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

    return new ResponseEntity<>(errorResponse, status);
  }
}
