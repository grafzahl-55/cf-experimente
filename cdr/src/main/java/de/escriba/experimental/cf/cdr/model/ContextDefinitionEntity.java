package de.escriba.experimental.cf.cdr.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author micha
 */
@Data
@Entity
public class ContextDefinitionEntity{

    @GeneratedValue @Id
    private Long id;

    @Column(length = 64, nullable = false)
    private String serviceType;
    
    @Column(length=1024, nullable = true)
    private String description;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags=new ArrayList<>();

    @OneToMany(targetEntity = ContextSourceEntity.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContextSourceEntity> sources=new ArrayList<>();

    public ContextDefinitionEntity serviceType(String serviceType){
        this.setServiceType(serviceType);
        return this;
    }
    public ContextDefinitionEntity description(String description) {
        this.setDescription(description);
        return this;
    }
    public ContextDefinitionEntity tags(String... tags){
        this.setTags(new ArrayList<>(Arrays.asList(tags)));
        return this;
    }

    public ContextDefinitionEntity sources(ContextSourceEntity... src){
        this.setSources(new ArrayList<>(Arrays.asList(src)));
        return this;
    }
    public ContextDefinitionEntity addSources(ContextSourceEntity... src){
        this.getSources().addAll(Arrays.asList(src));
        return this;
    }

    public ContextSourceEntity getSourceByRole(String role){
        if( this.sources==null ){
            return null;
        }
        for(ContextSourceEntity cse:sources){
            if( cse.getRole().equals(role) ){
                return cse;
            }
        }
        return null;
    }
}
