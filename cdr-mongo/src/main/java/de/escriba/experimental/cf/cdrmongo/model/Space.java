package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cflib.tags.Tagged;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @Override
    public Space tag(String... tags) {
        return this.tag(Arrays.asList(tags));
    }

    @Override
    public Space tag(Iterable<String> tags) {
        tags.forEach(this.tags::add);
        return this;
    }


}
