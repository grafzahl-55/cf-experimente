package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.CdrApplication;
import de.escriba.experimental.cf.cdr.model.ApplicationInstanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = CdrApplication.REPOSITORY_NAME_APPINSTANCE)
public interface ApplicationInstanceRepository extends CrudRepository<ApplicationInstanceEntity, Long> {
    /*
    @RestResource(path = "by-tags")
    List<ApplicationInstanceEntity> findByTags(@Param("tag") String tag);
*/
    @RestResource(path = "by-platform-and-name")
    Optional<ApplicationInstanceEntity> findFirstByPlatformAndApplicationName(@Param("platform") String platform,
                                                                              @Param("applicationName") String applicationName);

    @RestResource(path = "by-platform-and-type")
    List<ApplicationInstanceEntity> findByPlatformAndServiceType(@Param("platform") String platform,
                                                                 @Param("serviceType") String type);

    @RestResource(path = "by-platform")
    List<ApplicationInstanceEntity> findByPlatform(@Param("platform") String platform);

}
