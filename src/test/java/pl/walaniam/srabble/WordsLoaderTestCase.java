package pl.walaniam.srabble;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordsLoaderTestCase {

    protected static class WordsConsumerMock implements WordsConsumer {

        private final List<String> words = new ArrayList<>();

        public void add(String word) {
            words.add(word);
        }

        public int size() {
            return words.size();
        }
    }

    private final FastWordsLoader loader = new FastWordsLoader();

    @Test
    public void testLoadWordsFromFile() throws Exception {
        InputStream in = getClass().getResourceAsStream("words.txt");
        WordsConsumerMock consumer = new WordsConsumerMock();
        loader.loadWords(in, consumer, true);

        assertEquals(314, consumer.size());
    }

    @Test
    public void testLoadSingleLineWords() throws Exception {
        String wordsLine = "aaa bbb ccc ddd ee";

        InputStream in = new ByteArrayInputStream(wordsLine.getBytes());
        WordsConsumerMock consumer = new WordsConsumerMock();
        loader.loadWords(in, consumer, true);

        assertEquals(5, consumer.size());
    }
}
