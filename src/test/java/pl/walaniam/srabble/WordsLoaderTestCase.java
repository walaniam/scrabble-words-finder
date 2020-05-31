package pl.walaniam.srabble;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class WordsLoaderTestCase extends TestCase {
    
    protected static class WordsConsumerMock implements WordsConsumer {
        
        private final List<String> words = new ArrayList<String>();

        public void add(String word) {
            words.add(word);
        }

        public int size() {
            return words.size();
        }
    }
    
    private WordsLoader loader;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loader = newWordsLoader();
    }
    
    /**
     * Create words loader.
     * 
     * @return
     */
    protected abstract WordsLoader newWordsLoader();
    
    public void testLoadWordsFromFile() throws Exception {
        InputStream in = getClass().getResourceAsStream("words.txt");
        WordsConsumerMock consumer = new WordsConsumerMock();
        loader.loadWords(in, consumer, true);
        
        assertEquals(314, consumer.size());
    }
    
    public void testLoadSingleLineWords() throws Exception {
        String wordsLine = "aaa bbb ccc ddd ee";
        
        InputStream in = new ByteArrayInputStream(wordsLine.getBytes());
        WordsConsumerMock consumer = new WordsConsumerMock();
        loader.loadWords(in, consumer, true);
        
        assertEquals(5, consumer.size());
    }
}
