package de.escriba.experimental.cf.cdr.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author micha
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CONTEXT_DEFINITION",indexes = {
        @Index(columnList = "serviceType"),
        @Index(columnList = "contextName",unique = true)
})
@ToString(of = {"contextName","serviceType"})
public class ContextDefinitionEntity extends BaseEntity<ContextDefinitionEntity>{


    @Column(length = 255, nullable = false)
    private String contextName;

    @Column(length = 64, nullable = false)
    private String serviceType;
    

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags=new ArrayList<>();



    @OneToMany(targetEntity = ContextSourceEntity.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContextSourceEntity> sources=new ArrayList<>();

    @OneToMany(targetEntity = ContextDeploymentEntity.class, mappedBy = "contextDefinition")
    private List<ContextDeploymentEntity> deployedTo;

    public ContextDefinitionEntity serviceType(String serviceType){
        this.setServiceType(serviceType);
        return this;
    }
    public ContextDefinitionEntity name(String name){
        this.setContextName(name);
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

    @Override
    protected ContextDefinitionEntity self() {
        return this;
    }
}
