package pl.walaniam.srabble.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGroupingUtils {

    public static <T extends CharSequence> Map<Integer, List<T>> groupByLength(Collection<T> words,
                                                                               Comparator<T> comparator) {

        log.debug("Grouping {} words by length", words.size());
        final long startTime = System.currentTimeMillis();

        Map<Integer, List<T>> grouped = words.stream()
                .collect(Collectors.groupingBy(CharSequence::length));
        grouped.values().forEach(list -> Collections.sort(list, comparator));

        log.debug("{} words grouped in {} ms", words.size(), System.currentTimeMillis() - startTime);

        return grouped;
    }
}
