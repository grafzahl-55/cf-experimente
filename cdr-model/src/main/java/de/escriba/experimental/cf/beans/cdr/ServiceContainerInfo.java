package de.escriba.experimental.cf.beans.cdr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class ServiceContainerInfo extends BasicInfoObject<ServiceContainerInfo>{

    private String serviceType;

    public ServiceContainerInfo() {
        super(ServiceContainerInfo.class);
    }

    public ServiceContainerInfo(String name) {
        super(ServiceContainerInfo.class, name);
    }

    public ServiceContainerInfo(String name, String serviceType) {
        super(ServiceContainerInfo.class, name);
        this.serviceType=serviceType;
    }
    public ServiceContainerInfo(String name, String serviceType, String description) {
        super(ServiceContainerInfo.class, name,description);
        this.serviceType=serviceType;
    }
}
