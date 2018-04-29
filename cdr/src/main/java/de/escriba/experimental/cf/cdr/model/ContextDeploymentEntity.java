package de.escriba.experimental.cf.cdr.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"applicationInstance","contextDefinition"})
public class ContextDeploymentEntity extends BaseEntity<ContextDeploymentEntity> {

    @ManyToOne(targetEntity = ApplicationInstanceEntity.class, optional = false)
    private ApplicationInstanceEntity applicationInstance;

    @ManyToOne(targetEntity =  ContextDefinitionEntity.class,optional = false)
    private ContextDefinitionEntity  contextDefinition;


    public ContextDeploymentEntity(ApplicationInstanceEntity applicationInstance, ContextDefinitionEntity contextDefinition) {
        this.applicationInstance = applicationInstance;
        this.contextDefinition = contextDefinition;
    }

    @Override
    protected ContextDeploymentEntity self() {
        return this;
    }
}
