package de.escriba.experimental.cf.sbs.repository;

import de.escriba.experimental.cf.sbs.model.TagValue;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author micha
 */
@Component
public interface TagRepository extends PagingAndSortingRepository<TagValue, Long>{
    
    public List<TagValue> findByFileId(Long fileId);
    public Page<TagValue> findByTagNameAndTagValue(Pageable x,String tagName, String tagValue);
    
    
    @Modifying
    @Query("delete from TagValue tv where tv.fileId=?1")
    public void deleteByFileId(Long fileId);
}
