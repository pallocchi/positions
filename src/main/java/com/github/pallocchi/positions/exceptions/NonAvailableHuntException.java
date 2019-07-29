package com.github.pallocchi.positions.exceptions;

public class NonAvailableHuntException extends RuntimeException {

    public NonAvailableHuntException() {
        super("No available hunt for client");
    }

}
