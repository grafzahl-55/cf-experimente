package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.CdrApplication;
import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * @author micha
 */
@RepositoryRestResource(path = CdrApplication.REPOSITORY_NAME_CONTEXT)
public interface ContextDefinitionRepository extends CrudRepository<ContextDefinitionEntity, Long> {
    @RestResource(path = "by-tags")
    List<ContextDefinitionEntity> findByTags(@Param("tag") String tag);

    @RestResource(path = "by-type")
    List<ContextDefinitionEntity> findByServiceType(@Param("type") String serviceType);

    @RestResource(path = "by-name")
    Optional<ContextDefinitionEntity> findFirstByContextName(@Param("name") String contextName);

}
