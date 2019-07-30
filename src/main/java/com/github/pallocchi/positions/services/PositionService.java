package com.github.pallocchi.positions.services;

import com.github.pallocchi.positions.exceptions.NonAvailableHuntException;
import com.github.pallocchi.positions.model.Hunt;
import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.repositories.HuntRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Service to support Full Text Search on positions.
 */
@Service
public class PositionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_LOCATION = "location";

    private final EntityManager entityManager;

    private final HuntRepository huntRepository;

    @Autowired
    public PositionService(EntityManagerFactory entityManagerFactory, HuntRepository huntRepository) throws InterruptedException {

        this.entityManager = entityManagerFactory.createEntityManager();
        this.huntRepository = huntRepository;

        LOGGER.info("Initialize Lucene index to support Full Text Search");

        Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
    }

    public List<Position> search(int clientId) {

        // Find the hunts for given client.

        final List<Hunt> hunts = huntRepository.findByClientId(clientId);

        if (!hunts.isEmpty()) {

            // Right now we only support search by a single hunt,
            // but could be easily extended.

            if (hunts.size() > 1) {

                LOGGER.warn("Multiple hunts were found for client, using the first one");
            }

            return search(hunts.iterator().next());

        } else {

            throw new NonAvailableHuntException();
        }
    }

    /**
     * Retrieves positions matching given hunt.
     *
     * @param hunt the hunt to perform the search
     * @return the positions found for given hunt
     */
    @Transactional
    public List<Position> search(Hunt hunt) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder builder = fullTextEntityManager.getSearchFactory()
            .buildQueryBuilder()
            .forEntity(Position.class)
            .get();

        BooleanJunction<?> bool = builder.bool();

        if (hunt.getType() != null) {
            bool = bool.must(match(builder, FIELD_TYPE, hunt.getType()));
        }

        if (hunt.getName() != null) {
            bool = bool.must(match(builder, FIELD_NAME, hunt.getName()));
        }

        if (hunt.getLocation() != null) {
            bool = bool.must(match(builder, FIELD_LOCATION, hunt.getLocation()));
        }

        return fullTextEntityManager
            .createFullTextQuery(bool.createQuery(), Position.class)
            .getResultList();
    }

    /**
     * Build a query to match an specific field.
     *
     * @param builder the root query builder
     * @param field the field name
     * @param value the value to match
     * @return the query
     */
    private static Query match(QueryBuilder builder, String field, String value) {

        return builder
            .phrase()
            .onField(field)
            .sentence(value)
            .createQuery();
    }

}
