package de.escriba.experimental.cf.beans.cdr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtendedSpaceInfo extends SpaceInfo{

    private Map<String, ServiceContainerInfo> serviceContainers;

    public ExtendedSpaceInfo() {
    }

    public ExtendedSpaceInfo(String name) {
        super(name);
    }

    public ExtendedSpaceInfo(String name, String description) {
        super(name, description);
    }
}
