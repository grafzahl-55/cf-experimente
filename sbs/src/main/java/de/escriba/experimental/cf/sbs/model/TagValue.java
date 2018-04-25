package de.escriba.experimental.cf.sbs.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author micha
 */
@Data
@ToString
@Entity
@Table(indexes = {
    @Index(unique = false,columnList = "fileId"),
    @Index(unique = false,columnList = "tagName,tagValue")
})
public class TagValue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private String tagName;
    @NotNull
    private String tagValue;
    @NotNull
    private Long fileId;
    
    public TagValue() {
    }

    public TagValue(Long fileId,String tagName, String tagValue) {
        this.fileId = fileId;
        this.tagName = tagName;
        this.tagValue = tagValue;
    }
    
    
    
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

   
    
    
}
