package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the {@link Position} entity.
 */
public interface PositionRepository extends PagingAndSortingRepository<Position, Integer> {

    @Query("select count(p) > 0 from Position p where p.externalId = :externalId")
    boolean existsByExternalId(@Param("externalId") String externalId);

}
