package de.escriba.experimental.cf.cdr.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "APPLICATION_INSTANCE",indexes = {
        @Index(unique = true,columnList = "platform,applicationName")
})
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"applicationName","serviceType","platform","stage"})
public class ApplicationInstanceEntity extends BaseEntity<ApplicationInstanceEntity>{

    @Column(length = 128, nullable =    false)
    private String applicationName;

    @Column(length = 64, nullable = false)
    private String serviceType;


    @Column(length = 128, nullable = false)
    private String platform;

    @Column(length = 32,nullable = false)
    private String stage;
/*
    @ElementCollection
    private List<String> tags=new ArrayList<>();
*/

    @OneToMany(mappedBy = "applicationInstance", targetEntity = ContextDeploymentEntity.class)
    private List<ContextDeploymentEntity> deployments;

    public ApplicationInstanceEntity(String applicationName, String serviceType, String platform, String stage) {
        this.applicationName = applicationName;
        this.serviceType = serviceType;
        this.platform = platform;
        this.stage = stage;
    }

    @Override
    protected ApplicationInstanceEntity self() {
        return this;
    }

    public ApplicationInstanceEntity name(String name){
        this.applicationName=name;
        return this;
    }
    public ApplicationInstanceEntity serviceType(String type){
        this.serviceType=type;
        return this;
    }
    public ApplicationInstanceEntity platform(String platform){
        this.platform=platform;
        return this;
    }
    public ApplicationInstanceEntity stage(String stage){
        this.stage=stage;
        return this;
    }
    /*
    public ApplicationInstanceEntity tags(String... tags){
        this.setTags(new ArrayList<>(Arrays.asList(tags)));
        return this;
    }
*/

}
