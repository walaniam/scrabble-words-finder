package pl.walaniam.srabble;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import pl.walaniam.srabble.datastructures.TransactionAware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class FastWordsLoader implements WordsLoader {

    private int longestWordLength;

    @Override
    public void loadWords(final InputStream wordsStream, WordsConsumer consumer,
                          boolean toLowerCase) throws IOException {

        long start = System.currentTimeMillis();
        log.debug("Fast loading words from stream...");

        longestWordLength = 0;

        BufferedReader br = null;
        try {
            if (consumer instanceof TransactionAware) {
                ((TransactionAware) consumer).beginTransaction();
            }

            br = new BufferedReader(new InputStreamReader(wordsStream, WordsLoader.ENCODING));

            final StringBuilder wordBuffer = new StringBuilder();
            // 100kB buffer
            final int bufferSize = 100 * 1024;
            final char[] readBuffer = new char[bufferSize];
            int read = -1;

            // read characters from buffer
            while ((read = br.read(readBuffer, 0, bufferSize)) > -1) {

                for (int i = 0; i < read; i++) {
                    // create words from characters and add them to list
                    char c = readBuffer[i];
                    if (!Character.isWhitespace(c)) {
                        if (toLowerCase && Character.isUpperCase(c)) {
                            c = Character.toLowerCase(c);
                        }
                        wordBuffer.append(c);
                    } else {
                        addAndCleanBuffer(consumer, wordBuffer);
                    }
                }
            }

            addAndCleanBuffer(consumer, wordBuffer);

        } finally {
            if (consumer instanceof TransactionAware) {
                ((TransactionAware) consumer).commitTransaction();
            }
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(wordsStream);
        }

        log.debug("{} words loaded in {} ms", consumer.size(), System.currentTimeMillis() - start);
    }

    private void addAndCleanBuffer(WordsConsumer consumer, StringBuilder wordBuffer) {
        final int length = wordBuffer.length();
        if (length > 0) {
            consumer.add(wordBuffer.toString());
            wordBuffer.delete(0, length);
            longestWordLength = Math.max(longestWordLength, length);
        }
    }

    @Override
    public int getLongestWordLength() {
        return longestWordLength;
    }
}
