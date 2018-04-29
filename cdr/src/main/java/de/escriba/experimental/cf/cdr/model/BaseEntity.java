package de.escriba.experimental.cf.cdr.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
abstract public class BaseEntity<T extends BaseEntity> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1024, nullable = true)
    private String description;

    @CreatedDate
    private Date createdOn;

    @LastModifiedDate
    private Date modifiedOn;

    @Version
    private Long versionNumber;


    abstract protected T self();

    public T description(String description){
        this.description=description;
        return self();
    }
}
