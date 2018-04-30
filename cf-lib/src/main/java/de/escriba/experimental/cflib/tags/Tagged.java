package de.escriba.experimental.cflib.tags;

import java.util.Collection;
import java.util.Set;

public interface Tagged<T extends Tagged> {

    Set<String> getTags();
    void setTags(Collection<String> tags);

    T clearTags();

    /**
     * Adds new tags w/o removing the old ones
     * @param tags Tags
     * @return this
     */
    T tag(String... tags);
    T tag(Iterable<String> tags);

}
