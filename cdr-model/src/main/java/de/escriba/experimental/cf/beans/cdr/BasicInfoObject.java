package de.escriba.experimental.cf.beans.cdr;

import de.escriba.experimental.cflib.tags.Tagged;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
abstract public class BasicInfoObject<T extends Tagged<T>> implements Tagged<T>{


    private final Class<T> clazz;
    private final Set<String> tags=new HashSet<>();

    private String name;
    private String description;

    public BasicInfoObject(Class<T> clazz) {
        this.clazz = clazz;
    }
    public BasicInfoObject(Class<T> clazz, String name) {
        this(clazz,name,null);
    }
    public BasicInfoObject(Class<T> clazz, String name, String description) {
        this.clazz = clazz;
        this.name = name;
        this.description = description;
    }

    protected Class<T> getClazz(){
        return clazz;
    }

    @Override
    public void setTags(Collection<String> tags) {
        this.tags.clear();
        if(tags!=null){
            this.tags.addAll(tags);
        }
    }

    @Override
    public T clearTags() {
        this.tags.clear();
        return clazz.cast(this);
    }

    @Override
    public T tag(String... tags) {
        return this.tag(Arrays.asList(tags));
    }

    @Override
    public T tag(Iterable<String> tags) {
        if(tags!=null){
            tags.forEach(this.tags::add);
        }
        return this.clazz.cast(this);
    }
}
