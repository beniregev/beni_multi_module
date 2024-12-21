package com.beniregev.serviceshuffle.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ShuffleGlobalExceptionHandle {
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
      // Return a custom error message with HTTP 422 status
      return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
   }

   @ExceptionHandler(ArrayStoreException.class)
   public ResponseEntity<String> handleArrayStoreException(Exception ex) {
      // Return a custom error message with HTTP 422 status
      return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> handleGeneralException(Exception ex) {
      // Return a custom error message with HTTP 500 status
      return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
