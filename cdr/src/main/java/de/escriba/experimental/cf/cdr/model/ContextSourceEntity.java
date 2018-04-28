package de.escriba.experimental.cf.cdr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class ContextSourceEntity {
    @Id @GeneratedValue
    private Long id;
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

}
