package de.escriba.experimental.cf.beans.cdr;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class OrganizationInfo extends BasicInfoObject<OrganizationInfo> {


    public OrganizationInfo(){
        super(OrganizationInfo.class);
    }

    public OrganizationInfo(String name){
        super(OrganizationInfo.class, name);
    }
    public OrganizationInfo(String name, String description){
        super(OrganizationInfo.class,name,description);
    }

}
