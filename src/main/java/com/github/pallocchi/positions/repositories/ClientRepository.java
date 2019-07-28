package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for the {@link Client} entity.
 */
public interface ClientRepository extends
    PagingAndSortingRepository<Client, Integer>,
    PartialUpdateRepository<Client, Integer> {

}
