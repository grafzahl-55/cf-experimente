package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cflib.tags.TagMatcher;
import de.escriba.experimental.cflib.tags.Tagged;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Entspricht einem Space in Cloudfoundry
 * (Eingebettet in {@link Organization}.
 * Innerhalb der Organisation ist der Name des Space eindeutig.
 * (Muss programmatisch gewährleistet werden!)
 */
@Data
@NoArgsConstructor
public class Space implements Tagged<Space> {

    private String name;
    private String description;
    private Set<String> tags=new HashSet<>();
    private Map<String,ServiceContainer> serviceContainers;


    public Space(String name) {
        this.name = name;
    }

    public Space(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
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
    public Space clearTags() {
        this.tags.clear();
        return this;
    }

    @ Override
    public Space tag(String... tags) {
        return this.tag(Arrays.asList(tags));
    }

    @Override
    public Space tag(Iterable<String> tags) {
        tags.forEach(this.tags::add);
        return this;
    }

    public Space addContainer(ServiceContainer... containers){
        if( this.serviceContainers==null ){
            this.serviceContainers=new HashMap<>();
        }
        for( ServiceContainer sc:containers ) {
            this.serviceContainers.put(sc.getName(),sc);
        }
        return this;
    }

    public boolean containsServiceContainerName(String containerName){
        return this.serviceContainers!=null && this.serviceContainers.containsKey(containerName);
    }

    public Optional<ServiceContainer> getServiceContainer(String containerName){
        return Optional.ofNullable(
                (this.serviceContainers==null) ? null : this.serviceContainers.get(containerName)
        );
    }

    public Collection<ServiceContainer> getServiceContainersByType(String type){
        if( this.serviceContainers==null ){
            return null;
        } else {
            return this.serviceContainers.values().stream()
                    .filter(sc-> type.equals(sc.getServiceType()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Gegeben ein getaggetes Objekt
     * @param serviceType Benötigter Service Type
     * @param target Getaggetes Objelt zum Vergleich
     * @param <T> Typparameter
     * @return Den besten Kandidaten
     */
    public <T extends Tagged<T>> Optional<ServiceContainer> findBestMatch(String serviceType, Tagged<T> target){
        return TagMatcher.bestMatch(target,getServiceContainersByType(serviceType));
    }

}
