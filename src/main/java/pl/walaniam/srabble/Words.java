package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.combinatorics.Permutations;
import pl.walaniam.srabble.datastructures.HashBagDictionary;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Words {

    private final int longestWordLength;
    private final Permutations permutations;
    private final WordsDictionary dictionary;
    
    public Words(File input) throws IOException {
        this(new FileInputStream(input));
    }

    public Words(InputStream words) throws IOException {
        final WordsLoader wordsLoader = new FastWordsLoader();
//        dictionary = new WordsDictionaryImpl();
        dictionary = new HashBagDictionary();
        try (InputStream wordsStream = new BufferedInputStream(words)) {
            wordsLoader.loadWords(wordsStream, dictionary, true);
            longestWordLength = wordsLoader.getLongestWordLength();
            permutations = new Permutations(longestWordLength);
        }
    }

    public Set<String> findWords(final String letters, Integer wordLength) {

        long startTime = System.currentTimeMillis();
        log.debug("Searching matching words for letters: {}", letters);

        final Set<String> allMatched = new HashSet<>();

        if (wordLength == null) {
            List<CompletableFuture<Set<String>>> futures = new ArrayList<>();
            for (int i = letters.length(); i >= 2; i--) {
                int permutationLength = i;
                futures.add(CompletableFuture.supplyAsync(
                        () -> permutations.matchWithPermutations(letters, permutationLength, dictionary))
                );
            }
            CompletableFuture.allOf(futures.stream().toArray(size -> new CompletableFuture[size])).join();
            futures.stream()
                    .map(CompletableFuture::join)
                    .forEach(allMatched::addAll);
        } else {
            Set<String> matched = permutations.matchWithPermutations(letters, wordLength, dictionary);
            allMatched.addAll(matched);
        }

        log.debug("{} words found in {} ms", allMatched.size(), System.currentTimeMillis() - startTime);

        return allMatched;
    }

    public List<String> findStartingWith(String prefix, Integer wordLength) {

        final long startTime = System.currentTimeMillis();
        log.debug("Searching words starting with: {}", prefix);

        final List<String> matchedWords = new ArrayList<>();
        final char c = prefix.charAt(0);
        final int prefixLength = prefix.length();

        final Collection<String> startingWithWords = dictionary.getWordsStartingWith(c);
        if (startingWithWords != null) {
            for (String word : startingWithWords) {
                final int currentLength = word.length();
                if (word.startsWith(prefix)
                        && (wordLength == null || (wordLength != null && currentLength == wordLength))) {
                    matchedWords.add(word);
                }
                if (currentLength >= prefixLength
                        && word.substring(0, prefixLength).compareTo(prefix) > 0) {
                    break;
                }
            }
        }

        log.debug("{} found in {} ms", matchedWords.size(), System.currentTimeMillis() - startTime);

        return matchedWords;
    }

    /**
     * Get number of words
     * 
     * @return
     */
    public int getWordsCount() {
        return dictionary.size();
    }

    /**
     * Clear this object
     */
    public void clear() {
        dictionary.clear();
        System.gc();
    }

    /**
     * Get the longest word length
     * 
     * @return
     */
    public int getLongestWordLength() {
        return longestWordLength;
    }

}
