package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.utils.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;

/**
 * Interface for partial update operations on a repository for a specific type.
 *
 * @author Pablo Pallocchi
 */
@NoRepositoryBean
public interface PartialUpdateRepository<T, ID> extends CrudRepository<T, ID> {

    /**
     * Updates an existing entity, which must exists in the database.
     *
     * <p>The main purpose of this method is to provide a way to perform partial updates,
     * updating only those attributes which are not {@literal null}.
     *
     * @param id the entity id
     * @param entity the entity with the attributes to update as not null
     * @return the updated entity
     * @throws EntityNotFoundException if there is not entity with given id
     */
    default T update(ID id, T entity) {

        return findById(id)
            .map(e -> BeanUtils.copyNonNullProperties(entity, e))
            .map(this::save)
            .orElseThrow(EntityNotFoundException::new);
    }

}
