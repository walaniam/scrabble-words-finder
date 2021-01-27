package pl.walaniam.srabble;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
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

    private final WordsLoader loader;

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{new FastWordsLoader()},
                new Object[]{new DefaultWordsLoader()}
        );
    }

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
