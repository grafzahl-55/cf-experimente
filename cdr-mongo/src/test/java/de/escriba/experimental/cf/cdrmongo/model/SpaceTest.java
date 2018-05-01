package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cflib.tags.Tagged;
import de.escriba.experimental.cflib.tags.TaggedSupport;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


public class SpaceTest {

    @Test
    public void shouldFindBestMatchingContainer(){

        Space space=new Space("dev")
                .addContainer(
                        new ServiceContainer("c1","eis-runtime"),
                        new ServiceContainer("c2","eis-runtime").tag("CI"),
                        new ServiceContainer("c3","authorization-server")


                        );

        App app1=new App().tag("C1");
        Optional<ServiceContainer> opt=space.findBestMatch("eis-runtime",app1);
        assertThat(opt.isPresent()).isTrue();
        assertThat(opt.get().getName()).isEqualTo("c2");

        App app2=new App().tag("C1");
        opt=space.findBestMatch("authorization-server",app2);
        assertThat(opt.isPresent()).isTrue();
        assertThat(opt.get().getName()).isEqualTo("c3");

        App app3=new App().tag();
        opt=space.findBestMatch("eis-runtime",app3);
        assertThat(opt.isPresent()).isTrue();
        // Welcher? Weiß man nicht

        App app4=new App().tag();
        opt=space.findBestMatch("file-adapter",app4);
        assertThat(opt.isPresent()).isFalse();

    }


    private static class App implements Tagged<App>{
        private final TaggedSupport<App> delegate;

        public App() {
            this.delegate = new TaggedSupport(this);
        }

        @Override
        public Set<String> getTags() {
            return delegate.getTags();
        }

        @Override
        public void setTags(Collection<String> tags) {
            delegate.setTags(tags);
        }

        @Override
        public App clearTags() {
            return delegate.clearTags();
        }

        @Override
        public App tag(String... tags) {
            return delegate.tag(tags);
        }

        @Override
        public App tag(Iterable<String> tags) {
            return delegate.tag(tags);
        }
    }

}
