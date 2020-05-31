package pl.walaniam.srabble.util;

import gnu.trove.map.hash.THashMap;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class LetterGroupingUtils {

    public static <T extends CharSequence> Map<Integer, List<T>> groupByLength(
            Collection<T> words, Comparator<T> comparator) {

        log.debug("Grouping {} words by length", words.size());

        final long startTime = System.currentTimeMillis();

        final Map<Integer, List<T>> grouped = new THashMap<>();

        for (T w : words) {
            getList(w.length(), grouped).add(w);
        }

        grouped.values().forEach(list -> Collections.sort(list, comparator));

        log.debug("{} words grouped in {} ms", words.size(), System.currentTimeMillis() - startTime);

        return grouped;
    }

    public static <T extends CharSequence> Map<Character, List<T>> groupByStartingLetter(
            Collection<T> words, boolean isSorted, Comparator<T> comparator) {
        return groupByStartingLetter(words.iterator(), isSorted, comparator);
    }

    public static <T extends CharSequence> Map<Character, List<T>> groupByStartingLetter(
            Iterator<T> words, boolean isSorted, Comparator<T> comparator) {

        log.debug("Grouping words by starting letter");

        final long startTime = System.currentTimeMillis();

        final Map<Character, List<T>> grouped = new THashMap<>();

        int size = 0;
        while (words.hasNext()) {
            size++;
            T w = words.next();
            char c = w.charAt(0);
            getList(c, grouped).add(w);
        }

        if (!isSorted) {
            grouped.values().forEach(list -> Collections.sort(list, comparator));
        }

        log.debug("{} words grouped in {} ms", size, System.currentTimeMillis() - startTime);

        return grouped;
    }

    private static <T, V extends CharSequence> List<V> getList(T key, Map<T, List<V>> grouped) {
        List<V> list = grouped.get(key);
        if (list == null) {
            list = new ArrayList<>();
            grouped.put(key, list);
        }
        return list;
    }
}
