package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.CdrApplication;
import de.escriba.experimental.cf.cdr.model.ContextDeploymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false, path= CdrApplication.REPOSITORY_NAME_DEPLOYMENENT)
public interface ContextDeploymentRepository extends CrudRepository<ContextDeploymentEntity,Long> {
}
