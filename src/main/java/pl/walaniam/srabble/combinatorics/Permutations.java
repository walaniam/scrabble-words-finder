package pl.walaniam.srabble.combinatorics;

import gnu.trove.set.hash.THashSet;
import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.WordsDictionary;

import java.util.Arrays;
import java.util.Set;

@Slf4j
public class Permutations {

    private final int wordBufferSize;
    
    public Permutations(int bufferSize) {
        this.wordBufferSize = bufferSize;
    }
    
    public Set<String> choosePermutations(String elements, final int k) {
        elements = elements.toLowerCase();
        return choosePermutations(elements.toCharArray(), k, null);
    }
    
    public Set<String> matchWithPermutations(String elements, int k, WordsDictionary dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Dictionary is null");
        }
        elements = elements.toLowerCase();
        return choosePermutations(elements.toCharArray(), k, dictionary);
    }
    
    private Set<String> choosePermutations(char[] chars, int k, WordsDictionary dictionary) {

        final long startTime = System.currentTimeMillis();

        final int n = chars.length;
        if (k > n) {
            throw new IllegalArgumentException("k is greater than elementsSeq length");
        }

        if (log.isDebugEnabled()) {
            log.debug("Searching " + k + " element combination from " + new String(chars));
        }

        final int numOfSearched = (int) (factorial(n) / (factorial(n - k)));

        final Set<String> matched = new THashSet<>();
        
        if (log.isDebugEnabled()) {
            log.debug("Enumerating char sequence. Chars: "
                    + Arrays.toString(chars) + ", wordsBuffer size: "
                    + wordBufferSize);
        }
        
        enumerateCharSequence(chars, n, k, matched, new char[wordBufferSize], dictionary);

        if (dictionary == null && numOfSearched != matched.size()) {
            throw new IllegalStateException("Expected number of permutations was " + numOfSearched
                    + " but " + matched.size() + " was found");
        }

        log.debug("{} permutations found in {} ms", numOfSearched, System.currentTimeMillis() - startTime);

        return matched;
    }

    private static void enumerateCharSequence(char[] chars, int n, int k, Set<String> matched, char[] word,
                                              WordsDictionary dictionary) {

        if (k == 0) {
            
            int wordLength = 0;
            for (int i = n; i < chars.length; i++) {
                word[wordLength++] = chars[i];
            }

            if (wordLength > 0) {
                if (dictionary == null) {
                    matched.add(convertToString(word, wordLength));
                } else {
                    final String fromDictionary = dictionary.getFromDictionary(convertToString(word, wordLength));
                    if (fromDictionary != null) {
                        matched.add(fromDictionary);
                    }
                }
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            swap(chars, i, n - 1);
            enumerateCharSequence(chars, n - 1, k - 1, matched, word, dictionary);
            swap(chars, i, n - 1);
        }
    }
    
    /**
     * Copy "size" chars to new String
     * 
     * @param chars
     * @param size
     * @return
     */
    private static String convertToString(char[] chars, int size) {
        String result;
        if (size == chars.length) {
            result = new String(chars);
        } else {
            result = new String(chars, 0, size);
        }
        return result;
    }

    // helper function that swaps a[i] and a[j]
    private static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
    
    /**
     * Silnia ;)
     * 
     * @param n
     * @return
     */
    public static long factorial(final int n) {
        
        int nCopy = n; 
        
        if (nCopy < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        long factorial = 1;
        
        while(nCopy > 0) {
            factorial = factorial * nCopy--;
        }
        return factorial;
    }

}
