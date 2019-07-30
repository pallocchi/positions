package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.exceptions.TooManyHuntsException;
import com.github.pallocchi.positions.model.Hunt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for the {@link Hunt} entity.
 */
public interface HuntRepository extends
    PagingAndSortingRepository<Hunt, Integer>,
    PartialUpdateRepository<Hunt, Integer> {

    /**
     * Retrieves the hunts of the given client.
     *
     * @param clientId the client id
     * @return the client active hunts
     */
    List<Hunt> findByClientId(int clientId);

    /**
     * Retrieves if client has a hunt or not.
     *
     * @param clientId the client id
     * @return if hunt was found for client
     */
    @Query("select count(h) > 0 from Hunt h where h.client.id = :clientId")
    boolean existsByClientId(@Param("clientId") int clientId);

    /**
     * Creates a new hunt if given client does not have one already.
     *
     * @param hunt the hunt to create
     * @return the created hunt
     * @throws TooManyHuntsException if client has a hunt
     */
    default Hunt create(Hunt hunt) {

        final int clientId = hunt.getClient().getId();

        if (!existsByClientId(clientId)) {

            return save(hunt);
        }

        throw new TooManyHuntsException();
    }

}
