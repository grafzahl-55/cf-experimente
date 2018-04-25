package de.escriba.experimental.cf.sbs.repository;

import de.escriba.experimental.cf.sbs.model.FileInfo;
import de.escriba.experimental.cf.sbs.model.TagValue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author micha
 */
@Service
public class TagService {
    
    private final TagRepository tagRepository;
    private final FileInfoRepository fileRepository;
    
    public TagService(TagRepository tagRepository, FileInfoRepository fileRepository) {
        this.tagRepository = tagRepository;
        this.fileRepository = fileRepository;
    }
    
    @Transactional
    public void saveTags(FileInfo file){
        if(file.getId()==null){
            throw new IllegalStateException("Unsaved FileInfo");
        }
        List<TagValue> cur=tagRepository.findByFileId(file.getId());
        Set<String> allNames=new HashSet<>();
        cur.forEach(tv->{
            allNames.add(tv.getTagName());
            if(file.getTags().containsKey(tv.getTagName())){
                if( !file.getTags().get(tv.getTagName()).equals(tv.getTagValue()) ){
                    tv.setTagValue(file.getTags().get(tv.getTagName()));
                    tagRepository.save(tv);
                }
            } else {
                tagRepository.delete(tv);
            }
        });
        file.getTags().entrySet().forEach(entry->{
            if(!allNames.contains(entry.getKey())){
               tagRepository.save(new TagValue(file.getId(),entry.getKey(), entry.getValue()));
            }
        });
                
    }
    
    public void loadTags(FileInfo file){
        if(file.getId()!=null){
            List<TagValue> cur=tagRepository.findByFileId(file.getId());
            cur.forEach(tv->{
                file.tag(tv.getTagName(), tv.getTagValue());
            });
        }
    }
    
    @Transactional  
    public void removeTags(FileInfo file){
        if(file.getId()!=null){
            tagRepository.deleteByFileId(file.getId());
        }
    }
    
    public Page<FileInfo> findFilesByTagNameAndValue(Pageable p,String tagName, String tagValue){
        Page<TagValue> pg=tagRepository.findByTagNameAndTagValue(p,tagName, tagValue);
        return pg.map(tv->fileRepository.findById(tv.getFileId()).get());
    }
}
