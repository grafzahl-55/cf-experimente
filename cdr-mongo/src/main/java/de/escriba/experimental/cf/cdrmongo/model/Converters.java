package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cf.beans.cdr.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Converters {

    public OrganizationInfo toInfo(Organization org){
        return new OrganizationInfo(org.getName(), org.getDescription())
                .tag(org.getTags());
    }

    public ExtendedOrganizationInfo toExtendedInfo(Organization org){
        ExtendedOrganizationInfo xOrg=new ExtendedOrganizationInfo(org.getName(), org.getDescription());
        xOrg.setTags(org.getTags());
        Map<String,ExtendedSpaceInfo> m=new HashMap<>();
        if(org.getSpaces()!=null){
            org.getSpaces().forEach((name,space)->{
                m.put(name, toExtendedInfo(space));
            });
        }
        xOrg.setSpaces(m);
        return xOrg;
    }

    public ServiceContainerInfo toInfo(ServiceContainer sc){
        ServiceContainerInfo si= new ServiceContainerInfo(sc.getName(), sc.getServiceType(),sc.getDescription());
        si.tag(sc.getTags());
        return si;
    }

    public void patch(ServiceContainerInfo si, ServiceContainer sc){
        sc.setDescription(si.getDescription());
        sc.setTags(si.getTags());
        sc.setServiceType(si.getServiceType());
        sc.setDescription(si.getDescription());
    }

    public void patch(OrganizationInfo orgInfo, Organization org){
        org.setDescription(orgInfo.getDescription());
        org.setTags(orgInfo.getTags());
    }

    public SpaceInfo toInfo(Space space){
        return new SpaceInfo(space.getName(), space.getDescription()).tag(space.getTags());
    }

    public ExtendedSpaceInfo toExtendedInfo(Space space){
        ExtendedSpaceInfo xSPaceInfo=new ExtendedSpaceInfo(space.getName(), space.getDescription());
        xSPaceInfo.tag(space.getTags());
        Map<String,ServiceContainerInfo> m=new HashMap<>();
        if( space.getServiceContainers()!=null ){
            space.getServiceContainers().forEach((name, ci)->{
                m.put(name, toInfo(ci));
            });
        }
        xSPaceInfo.setServiceContainers(m);
        return xSPaceInfo;
    }
    public void patch(SpaceInfo spaceInfo, Space space){
        space.setDescription(spaceInfo.getDescription());
        space.setTags(spaceInfo.getTags());
    }

}
