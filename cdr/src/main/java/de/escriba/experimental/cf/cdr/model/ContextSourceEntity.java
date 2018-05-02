package de.escriba.experimental.cf.cdr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name="CONTEXT_SOURCE")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class ContextSourceEntity extends BaseEntity<ContextSourceEntity>{
/*
    @ManyToOne
    private ContextDefinitionEntity parent;
*/
    @Column(length = 128)
    private String role;

    @Column(length = 128)
    private String contentType;


    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] source;


    public ContextSourceEntity role(String role){
        this.setRole(role);
        return this;
    }


    public ContextSourceEntity source(byte[] data, String contentType){
        this.setSource(data);
        this.setContentType(contentType);
        return this;
    }


    @Override
    protected ContextSourceEntity self() {
        return this;
    }
}
