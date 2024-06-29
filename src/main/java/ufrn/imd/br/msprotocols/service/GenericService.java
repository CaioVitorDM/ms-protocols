package ufrn.imd.br.msprotocols.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestHeader;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;
import ufrn.imd.br.msprotocols.model.BaseEntity;
import ufrn.imd.br.msprotocols.repository.GenericRepository;
import ufrn.imd.br.msprotocols.utils.exception.ResourceNotFoundException;
import ufrn.imd.br.msprotocols.utils.validators.GenericEntityValidator;


/**
 * A generic service interface defining common operations for entities in the
 * application.
 *
 * @param <E>   The type of the entity extending BaseEntity.
 * @param <DTO> The type of the DTO (Data Transfer Object) associated with the
 *              entity.
 */
public interface GenericService<E extends BaseEntity, DTO> {

    /**
     * Gets the repository associated with the entity.
     *
     * @return The repository for the entity.
     */
    GenericRepository<E> getRepository();

    /**
     * Gets the DTO mapper associated with the entity.
     *
     * @return The DTO mapper for the entity.
     */
    DtoMapper<E, DTO> getDtoMapper();

    /**
     * Retrieves all entities using pagination.
     *
     * @param pageable Pagination information.
     * @return Page of DTOs representing the entities.
     */
    default Page<DTO> findAll(Pageable pageable) {
        Page<E> entityPage = getRepository().findAll(pageable);
        return new PageImpl<>(getDtoMapper().toDto(entityPage.getContent()), pageable, entityPage.getTotalElements());
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return The DTO representing the entity.
     * @throws ResourceNotFoundException if the ID is not found.
     */
    default DTO findById(Long id) {

        E entity = getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        return getDtoMapper().toDto(entity);
    }

    /**
     * Creates a new entity based on the provided DTO.
     *
     * @param dto The DTO representing the entity to be created.
     * @return The DTO representing the created entity.
     */
    default DTO create(DTO dto, String token) {
        E entity = getDtoMapper().toEntity(dto);
        validateBeforeSave(entity, token);
        return getDtoMapper().toDto(getRepository().save(entity));
    }

    /**
     * Updates an existing entity based on the provided DTO.
     *
     * @param id  The ID of the entity to be updated.
     * @param dto The DTO representing the updated entity.
     * @return The DTO representing the updated entity.
     */
    default DTO update(Long id, DTO dto, String token) {

        var entityExists = getRepository().existsById(id);
        if (!entityExists)
            throw new ResourceNotFoundException("Id not found: " + id);

        E updatedEntity = getDtoMapper().toEntity(dto);
        updatedEntity.setId(id);
        validateBeforeUpdate(updatedEntity, token);

        return getDtoMapper().toDto(getRepository().save(updatedEntity));
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to be deleted.
     */
    default void deleteById(Long id) {
        getRepository().deleteById(id);
    }

    /**
     * Validates the entity before saving.
     *
     * @param entity The entity to be validated.
     */
    public abstract void validateBeforeSave(E entity, String token);

    /**
     * Validates the entity before updating.
     *
     * @param entity The entity to be validated.
     */
    public abstract void validateBeforeUpdate(E entity, String token);
}
