package de.escriba.experimental.cflib.tags;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Optional;

public class TagMatcher {


    /**
     *
     * @param in Input taggable
     * @param candidate The candidate to match
     * @return Match result (number of common tags)
     */
    public static int score(Tagged<?> in, Tagged<?> candidate){
        assert in!=null ;
        assert candidate!=null;
        int score=0;
        for (String tag: in.getTags()){
            if(candidate.getTags().contains(tag)){
                score++;
            }
        }
        return score;
    }

    public static <T extends Tagged<T>> Optional<T> bestMatch(Tagged<?> in, Iterable<T> candidates){
        assert in!=null;
        if(candidates==null){
            return Optional.empty();
        }
        T best=null;
        int bestScore=-1;
        for(T candidate:candidates){
            int score=score(in,candidate);
            if( score>=bestScore ){
                best=candidate;
                bestScore=score;
            }
        }
        return Optional.ofNullable(best);
    }

}
