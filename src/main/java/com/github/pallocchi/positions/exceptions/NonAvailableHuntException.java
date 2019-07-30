package com.github.pallocchi.positions.exceptions;

/**
 * Exception when a hunt is not defined for a client.
 *
 * @see com.github.pallocchi.positions.services.PositionService
 */
public class NonAvailableHuntException extends RuntimeException {

    public NonAvailableHuntException() {
        super("No available hunt for client");
    }

}
