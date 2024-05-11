package ufrn.imd.br.msprotocols.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ufrn.imd.br.msprotocols.model.BaseEntity;

import java.util.Optional;

/**
 * A generic repository interface defining common CRUD operations for entities
 * in the application.
 *
 * @param <T> The type of the entity extending BaseEntity.
 *
 * @noRepositoryBean means that the interface won't be instanced as
 * a repository bean by Spring.
 */
@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    /**
     * Overrides the default methods of deletion (by id, object and object list) to
     * do Logical Deletion instead of Physical deletion, does it by changing
     * the "active" flag.
     *
     */

    @Override
    @Transactional
    default void deleteById(Long id){
        Optional<T> entity = findById(id);
        if(entity.isPresent()){
            entity.get().setActive(false);
            save(entity.get());
        }
    }

    @Override
    @Transactional
    default void delete(T obj){
        obj.setActive(false);
        save(obj);
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> objects){
        objects.forEach(entity -> deleteById(entity.getId()));
    }
}
