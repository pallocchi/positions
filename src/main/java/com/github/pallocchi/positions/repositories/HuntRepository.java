package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Hunt;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Repository for the {@link Hunt} entity.
 */
public interface HuntRepository extends
    PagingAndSortingRepository<Hunt, Integer>,
    PartialUpdateRepository<Hunt, Integer> {

    List<Hunt> findByClientId(int clientId);

}
