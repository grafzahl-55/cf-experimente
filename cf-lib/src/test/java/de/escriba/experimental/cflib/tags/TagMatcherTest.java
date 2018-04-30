package de.escriba.experimental.cflib.tags;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class TagMatcherTest {

    @Rule
    public ExpectedException exception=ExpectedException.none();

    @Test
    public void shouldFailWithNullAsFirstArgument(){
        exception.expect(AssertionError.class);
        TaggedSupportTest.TestClass candidate=new TaggedSupportTest.TestClass();
        TagMatcher.score(null,candidate);
    }

    @Test
    public void shouldFailWithNullAsSecondArgument(){
        exception.expect(AssertionError.class);
        TaggedSupportTest.TestClass in=new TaggedSupportTest.TestClass();
        TagMatcher.score(in,null);
    }

    @Test
    public void shouldScoreReturnZeroOnEMptyTags() {
        TaggedSupportTest.TestClass in = new TaggedSupportTest.TestClass()
                .tag("a", "b");
        TaggedSupportTest.TestClass candidate = new TaggedSupportTest.TestClass();
        assertThat(TagMatcher.score(in, candidate)).isEqualTo(0);
        assertThat(TagMatcher.score(candidate, in)).isEqualTo(0);
    }

    @Test
    public void shouldCountNumberOfCommonTags(){
        TaggedSupportTest.TestClass in = new TaggedSupportTest.TestClass()
                .tag("a", "b");
        TaggedSupportTest.TestClass candidate = new TaggedSupportTest.TestClass()
                .tag("c","d");
        assertThat(TagMatcher.score(in,candidate)).isEqualTo(0);

        candidate = new TaggedSupportTest.TestClass()
                .tag("b","d");
        assertThat(TagMatcher.score(in,candidate)).isEqualTo(1);

        candidate = new TaggedSupportTest.TestClass()
                .tag("b","a","e","f");
        assertThat(TagMatcher.score(in,candidate)).isEqualTo(2);

    }

    @Test
    public void bestMatchShouldFailOnNullInput(){
        exception.expect(AssertionError.class);
        TaggedSupportTest.TestClass candidate = new TaggedSupportTest.TestClass()
                .tag("c","d");
        TagMatcher.bestMatch(null, Collections.singleton(candidate));
    }

    @Test
    public void bestMatchShouldReturnEmptyOnNullCandidate(){
        TaggedSupportTest.TestClass in = new TaggedSupportTest.TestClass()
                .tag("c","d");

        Optional<?> best=TagMatcher.bestMatch(in,null);
        assertThat(best).isEmpty();

    }

    @Test
    public void bestMatchShouldReturnEmptyOnEmptyCandidate(){
        TaggedSupportTest.TestClass in = new TaggedSupportTest.TestClass()
                .tag("c","d");
        List<TaggedSupportTest.TestClass> empty=new ArrayList<>();
        Optional<TaggedSupportTest.TestClass> best=TagMatcher.bestMatch(in,empty);
        assertThat(best).isEmpty();

    }


    private static List<String> ALL_TAGS=new ArrayList<>();
    private static Random RND=new Random();
    @BeforeClass
    public static void setup(){
        for(int i=0; i<10; i++){
            ALL_TAGS.add("TAG-"+i);
        }
    }

    private String pickRandomTag(){
        return ALL_TAGS.get(RND.nextInt(ALL_TAGS.size()));
    }
    private TaggedSupportTest.TestClass createRandom(int numTags){
        TaggedSupportTest.TestClass tc=new TaggedSupportTest.TestClass();
        for(int i=0; i<numTags; i++){
            tc.tag(pickRandomTag());
        }
        return tc;
    }


    @Test
    public void bestMatchShouldReturnMaximumScore(){
        TaggedSupportTest.TestClass in=createRandom(5);
        List<TaggedSupportTest.TestClass> candidates=new ArrayList<>();
        for(int i=0; i<10; i++){
            candidates.add(createRandom(5));
        }

        Optional<TaggedSupportTest.TestClass> best=TagMatcher.bestMatch(in,candidates);
        assertThat(best).isPresent();
        int bestScore=TagMatcher.score(in,best.get());
        assertThat(candidates).contains(best.get());
        candidates.forEach( candidate->{
            int score=TagMatcher.score(in,candidate);
            assertThat(bestScore).isGreaterThanOrEqualTo(score);
        });
    }
}
