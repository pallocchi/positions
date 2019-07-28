package com.github.pallocchi.positions.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * Handler to return a {@link HttpStatus#BAD_REQUEST} when a {@link ConstraintViolationException} occurs.
 *
 * <p>{@link ConstraintViolationException} is thrown when a bean validation fails.
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody ErrorMessage handleConstraintViolationException(ConstraintViolationException e) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Response when an error occurs.
     */
    public static class ErrorMessage {

        /**
         * The HTTP status code
         */
        private final int status;

        /**
         * The error message to return to the client
         */
        private final String message;

        public ErrorMessage(HttpStatus status, String message) {
            this.status = status.value();
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

    }

}
