package com.github.pallocchi.positions.exceptions;

/**
 * Exception when there are multiple hunts for same client.
 *
 * @see com.github.pallocchi.positions.repositories.HuntRepository
 */
public class TooManyHuntsException extends UnprocessableEntityException {

    public TooManyHuntsException() {
        super("Client has a hunt already");
    }

}
