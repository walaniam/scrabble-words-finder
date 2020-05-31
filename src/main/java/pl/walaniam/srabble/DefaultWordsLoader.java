package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class DefaultWordsLoader implements WordsLoader {

    private final AtomicInteger longestWordLength = new AtomicInteger();

    /**
     * {@inheritDoc}
     */
    public void loadWords(final InputStream wordsStream, WordsConsumer consumer, boolean toLowerCase) throws IOException {

        log.debug("Loading words from stream...");

        final long start = System.currentTimeMillis();

        try (BufferedReader bw = new BufferedReader(new InputStreamReader(wordsStream))) {
            bw.lines()
                    .map(String::trim)
                    .map(it -> it.split(" "))
                    .flatMap(Stream::of)
                    .peek(word -> longestWordLength.set(Math.max(longestWordLength.get(), word.length())))
                    .forEach(consumer::add);
        }

        log.debug("{} words loaded in {} ms", consumer.size(), System.currentTimeMillis() - start);
    }

    /**
     * {@inheritDoc}
     */
    public int getLongestWordLength() {
        return longestWordLength.get();
    }
}
