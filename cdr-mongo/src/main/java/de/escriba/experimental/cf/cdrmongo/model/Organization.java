package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cflib.tags.Tagged;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Entspricht einer Org in Cloudfoundry
 */
@Document
@Data
@NoArgsConstructor
public class Organization implements Tagged<Organization> {


    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private String description;
    private Set<String> tags=new HashSet<>();
    private Map<String,Space> spaces=new HashMap<>();

    public Organization(String name) {
        this.name = name;
    }

    public Organization(String name, String description) {
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

    public boolean containsSpace(String name){
        return (this.spaces != null) && this.spaces.containsKey(name);
    }

    public Optional<Space> getSpaceByName(String name){
        return this.spaces==null ? Optional.empty() : Optional.ofNullable(spaces.get(name));
    }

    @Override
    public Organization clearTags() {
        this.tags.clear();
        return this;
    }

    @Override
    public Organization tag(String... tags) {
        return this.tag(Arrays.asList(tags));
    }

    @Override
    public Organization tag(Iterable<String> tags) {
        tags.forEach(this.tags::add);
        return this;
    }

    public Organization addSpace(Space space){
        if(this.spaces==null){
            this.spaces=new HashMap<>();
        }
        this.spaces.put(space.getName(),space);
        return this;
    }
    public Organization removeSpace(String name){
        if(this.spaces!=null){
            this.spaces.remove(name);
        }
        return this;
    }

}
