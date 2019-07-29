package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Position;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for the {@link Position} entity.
 */
public interface PositionRepository extends PagingAndSortingRepository<Position, Integer> {

}
