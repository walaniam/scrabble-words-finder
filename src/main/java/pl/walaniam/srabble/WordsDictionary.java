package pl.walaniam.srabble;

import pl.walaniam.srabble.datastructures.Clearable;

import java.util.Collection;

public interface WordsDictionary extends Clearable, WordsConsumer {
    /**
     * Get the word from dictionary.
     * 
     * @param word
     * @return word if exists or null
     */
    String getFromDictionary(String word);

    Collection<String> getWordsStartingWith(char letter);
    
    int size();
}
