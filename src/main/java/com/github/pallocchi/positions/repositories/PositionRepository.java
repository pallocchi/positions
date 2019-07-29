package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Position;

public interface PositionRepository {

    /**
     * Saves position.
     *
     * @param position the position
     */
    void save(Position position);

}
