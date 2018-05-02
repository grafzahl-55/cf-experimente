package de.escriba.experimental.cf.beans.cdr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class ExtendedOrganizationInfo extends OrganizationInfo{

    private Map<String,ExtendedSpaceInfo> spaces;


    public ExtendedOrganizationInfo() {
    }

    public ExtendedOrganizationInfo(String name) {
        super(name);
    }

    public ExtendedOrganizationInfo(String name, String description) {
        super(name, description);
    }

    }
