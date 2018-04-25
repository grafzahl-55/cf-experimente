package de.escriba.experimental.cf.sbs.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author micha
 */
@Data
@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String name;
    
    private String contentType;
    
    private long contentLength;
    
    @Transient
    private Map<String,String> tags=new HashMap<>();
    
    @ManyToOne()
    private FolderInfo folder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public FolderInfo getFolder() {
        return folder;
    }

    public void setFolder(FolderInfo folder) {
        this.folder = folder;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
    
    
    public FileInfo tag(String name, String value){
        this.tags.put(name, value);
        return this;
    }
    
    
}
