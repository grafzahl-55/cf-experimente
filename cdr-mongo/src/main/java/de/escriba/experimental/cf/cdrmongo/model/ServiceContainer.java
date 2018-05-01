package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cflib.tags.Tagged;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert eine Anwendung, die mehrere Services eines bestimmten Typs
 * mit verschiedenen Konfigurationen enthält (z.B. eis-container)
 * Im CF-Umfeld entspäche das einer Applikation, deren Name innerhalb des Space eindutig
 * sein muss.
 */
@Data
@NoArgsConstructor
public class ServiceContainer implements Tagged<ServiceContainer> {

    private String name;
    private String serviceType;
    private String description;
    private Set<String> tags=new HashSet<>();

    public ServiceContainer(String name, String serviceType) {
        this.name = name;
        this.serviceType = serviceType;
    }

    public ServiceContainer(String name, String serviceType, String description) {
        this.name = name;
        this.serviceType = serviceType;
        this.description = description;
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(Collection<String> tags) {
        this.tags.clear();
        if(tags!=null){
            this.tags.addAll(tags);
        }
    }

    @Override
    public ServiceContainer clearTags() {
        this.tags.clear();
        return this;
    }

    @Override
    public ServiceContainer tag(String... tags) {
        return this.tag(Arrays.asList(tags));
    }

    @Override
    public ServiceContainer tag(Iterable<String> tags) {
        if( tags!=null ){
            tags.forEach(this.tags::add);
        }
        return this;
    }
}
