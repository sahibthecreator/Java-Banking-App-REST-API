package com.bank.app.restapi.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.app.restapi.dto.ExceptionDTO;
import com.sun.jdi.request.InvalidRequestStateException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

        // UNAUTHORIZED 401
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ExceptionDTO> BadCredentialsException(BadCredentialsException ex, WebRequest request) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ExceptionDTO(
                                HttpStatus.UNAUTHORIZED.value(),
                                ex.getClass().getName(),
                                "Invalid email or password"));
        }

        // NOT FOUND 404
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ExceptionDTO> resourceNotFoundException(EntityNotFoundException ex, WebRequest request) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ExceptionDTO(
                                HttpStatus.NOT_FOUND.value(),
                                ex.getClass().getName(),
                                ex.getMessage()));
        }

        // BAD REQUEST 400
        @ExceptionHandler(value = { IllegalArgumentException.class,
                InvalidRequestStateException.class })
        public ResponseEntity<ExceptionDTO> IllegalArgumentException(Exception ex, WebRequest request) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ExceptionDTO(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getClass().getName(),
                                ex.getMessage()));
        }

        // BAD REQUEST 400 for @Valid annotation exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
                List<String> errorMessages = ex.getBindingResult().getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), "Validation Error",
                                errorMessages.get(0)));
        }

        // SERVER ERROR 500
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionDTO> globalExceptionHandler(Exception ex, WebRequest request) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ExceptionDTO(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                ex.getClass().getName(),
                                ex.getMessage()));
        }

}
