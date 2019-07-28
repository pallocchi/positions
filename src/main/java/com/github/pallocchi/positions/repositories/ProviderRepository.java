package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Provider;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for the {@link Provider} entity.
 */
public interface ProviderRepository extends
    PagingAndSortingRepository<Provider, Integer>,
    PartialUpdateRepository<Provider, Integer> {

}
