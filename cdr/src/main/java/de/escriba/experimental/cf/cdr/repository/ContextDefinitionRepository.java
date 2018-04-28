package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 *
 * @author micha
 */
@RepositoryRestResource(path = "contexts")
public interface ContextDefinitionRepository extends CrudRepository<ContextDefinitionEntity, Long>{

    List<ContextDefinitionEntity> findByTags(String tag);

}
