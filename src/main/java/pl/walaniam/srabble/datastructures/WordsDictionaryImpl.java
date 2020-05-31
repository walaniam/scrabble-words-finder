package pl.walaniam.srabble.datastructures;

import pl.walaniam.srabble.WordsDictionary;

import java.util.*;

public class WordsDictionaryImpl implements WordsDictionary {

    private final Map<Character, Set<String>> words = new HashMap<>();

    @Override
    public String getFromDictionary(String word) {
        return bucketOf(word).contains(word) ? word.toLowerCase() : null;
    }

    @Override
    public Collection<String> getWordsStartingWith(char letter) {
        return Collections.unmodifiableCollection(bucketOf(letter));
    }

    @Override
    public void add(String word) {
        bucketOf(word).add(word.toLowerCase());
    }

    private Set<String> bucketOf(String word) {
        return bucketOf(word.charAt(0));
    }

    private Set<String> bucketOf(char firstLetter) {
        Character c = Character.toLowerCase(firstLetter);
        Set<String> bucket = words.get(c);
        if (bucket == null) {
            bucket = new HashSet<>();
            words.put(c, bucket);
        }
        return bucket;
    }

    @Override
    public int size() {
        return words.values().stream().map(Collection::size).reduce(0, Integer::sum);
    }

    @Override
    public void clear() {
        words.clear();
    }
}
