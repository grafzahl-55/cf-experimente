package de.escriba.experimental.cflib.tags;

import java.util.*;

/**
 * Useful as delegate
 */
public class TaggedSupport<T extends Tagged> implements Tagged<T> {

    final private T outer;
    final private Set<String> tags = new HashSet<>();

    public TaggedSupport(T outer) {
        this.outer = outer;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(Collection<String> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }


    @Override
    public T clearTags() {
        this.tags.clear();
        return self();
    }

    @Override
    public T tag(String... tags) {
        this.getTags().addAll(Arrays.asList(tags));
        return self();
    }

    @Override
    public T tag(Iterable<String> tags) {
        if (tags != null) {
            tags.forEach(this.tags::add);
        }
        return self();
    }

    private T self() {
        return outer;
    }
}
