package com.github.pallocchi.positions.exceptions;

public class ProviderNotFoundException extends RuntimeException {

    public ProviderNotFoundException() {
        super("Provider not found");
    }

}
