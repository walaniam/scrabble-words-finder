package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.combinatorics.Permutations;
import pl.walaniam.srabble.datastructures.CompactCharSequence;
import pl.walaniam.srabble.datastructures.TransactionAware;
import pl.walaniam.srabble.datastructures.TransactionalHashBag;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static pl.walaniam.srabble.util.LetterGroupingUtils.groupByStartingLetter;

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

    /**
     * Hash bag dictionary.
     */
    private static class HashBagDictionary implements WordsDictionary, WordsConsumer, TransactionAware {

        private final TransactionalHashBag<CompactCharSequence> bag = new TransactionalHashBag<>();

        private Map<Character, List<CompactCharSequence>> byStartingLetter;

        public String getFromDictionary(String word) {
            
            CompactCharSequence searched = new CompactCharSequence(word);

            CompactCharSequence found = null;
            Iterator<CompactCharSequence> values = bag.get(searched.hashCode());
            if (values != null) {
                while (values.hasNext()) {
                    CompactCharSequence next = values.next();
                    if (searched.equals(next)) {
                        found = next;
                        break;
                    }
                }
            }
            
            return (found == null) ? null : word;
        }

        public Collection<String> getWordsStartingWith(char letter) {

            if (byStartingLetter == null) {
                byStartingLetter = groupByStartingLetter(bag.iterator(), false, CompactCharSequence.COMPARATOR);
            }

            return CompactCharSequence.asStringCollection(byStartingLetter.get(letter));
        }

        public void clear() {
            bag.clear();
        }

        public void add(String word) {
            bag.add(new CompactCharSequence(word));
        }

        public void beginTransaction() {
            bag.beginTransaction();
        }

        public void commitTransaction() {
            bag.commitTransaction();
        }

        public int size() {
            return bag.size();
        }
    }

}
