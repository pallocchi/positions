package com.github.pallocchi.positions.exceptions;

/**
 * Exception to be mapped as a {@link org.springframework.http.HttpStatus#UNPROCESSABLE_ENTITY}.
 */
public abstract class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

}
