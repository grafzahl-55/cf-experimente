package de.escriba.experimental.cflib.tags;

import de.escriba.experimental.cflib.tags.Tagged;
import de.escriba.experimental.cflib.tags.TaggedSupport;
import lombok.experimental.Delegate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TaggedSupportTest {


    @Test
    public void tagsShouldNeverBeNull(){
        TestClass tc=new TestClass();
        assertThat(tc.getTags()).isNotNull();

        tc.setTags(null);
        assertThat(tc.getTags()).isNotNull();

    }
    @Test
    public void tagsAreInitiallyEmpty(){
        TestClass tc=new TestClass();
        assertThat(tc.getTags()).isEmpty();
    }

    @Test
    public void tagsShouldBeUnique(){
        TestClass tc=new TestClass();
        tc.setTags(Arrays.asList("1","2","2","3","1"));
        assertThat(tc.getTags()).containsExactlyInAnyOrder("1","2","3");
    }

    @Test
    public void tagsCanBeSetFkuently(){
        TestClass tc=new TestClass();
        tc.tag("1","2");
        assertThat(tc.getTags()).containsExactlyInAnyOrder("1","2");
        tc.clearTags()
                .tag("3")
                .tag(Arrays.asList("5","4"))
                .tag("5","6","7");
        assertThat(tc.getTags()).containsExactlyInAnyOrder("3","4","5","6","7");
        assertThat(tc.getTags().size()).isEqualTo(5);
    }


    public static class TestClass implements Tagged<TestClass> {
        private final TaggedSupport<TestClass> delegate;

        public TestClass() {
            this.delegate = new TaggedSupport<>(this);
        }



        @Override
        public Set<String> getTags() {
            return delegate.getTags();
        }


        @Override
        public TestClass clearTags() {
            return delegate.clearTags();
        }

        @Override
        public TestClass tag(String... tags) {
            return delegate.tag(tags);
        }

        @Override
        public TestClass tag(Iterable<String> tags) {
            return delegate.tag(tags);
        }

        @Override
        public void setTags(Collection<String> tags) {
            delegate.setTags(tags);
        }

    }

}
