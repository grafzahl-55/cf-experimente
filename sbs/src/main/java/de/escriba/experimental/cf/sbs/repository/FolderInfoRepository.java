package de.escriba.experimental.cf.sbs.repository;

import de.escriba.experimental.cf.sbs.model.FolderInfo;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author micha
 */
@Component
public interface FolderInfoRepository extends CrudRepository<FolderInfo, Long>{
    
    
    public Optional<FolderInfo> findByName(String name);
    
}
