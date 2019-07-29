package com.github.pallocchi.positions.services;

import com.github.pallocchi.positions.model.Position;

import java.util.List;

/**
 * Service to import positions from specific {@link com.github.pallocchi.positions.model.Provider}.
 */
interface ProviderService {

    /**
     * Retrieves the open positions from a provider.
     *
     * @return the open positions found
     */
    List<Position> findOpenPositions();

}
