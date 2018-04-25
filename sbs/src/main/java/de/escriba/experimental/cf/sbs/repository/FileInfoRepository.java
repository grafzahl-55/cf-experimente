
package de.escriba.experimental.cf.sbs.repository;

import de.escriba.experimental.cf.sbs.model.FileInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author micha
 */
@Component
public interface FileInfoRepository extends PagingAndSortingRepository<FileInfo, Long>{
    
}
