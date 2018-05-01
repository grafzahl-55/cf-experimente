package de.escriba.experimental.cf.beans.cdr;


import lombok.Data;

@Data
public class SpaceInfo extends BasicInfoObject<SpaceInfo> {


    public SpaceInfo(){
        super(SpaceInfo.class);
    }

    public SpaceInfo(String name){
        super(SpaceInfo.class, name);
    }
    public SpaceInfo(String name, String description){
        super(SpaceInfo.class,name,description);
    }

}
