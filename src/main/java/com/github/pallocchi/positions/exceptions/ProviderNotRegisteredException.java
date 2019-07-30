package com.github.pallocchi.positions.exceptions;

/**
 * Exception when a provider is not registered for import positions.
 *
 * @see com.github.pallocchi.positions.services.ImportService
 */
public class ProviderNotRegisteredException extends UnprocessableEntityException {

    public ProviderNotRegisteredException() {
        super("Provider not registered");
    }

}
