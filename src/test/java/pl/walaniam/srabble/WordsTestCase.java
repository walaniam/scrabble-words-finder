package pl.walaniam.srabble;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

public class WordsTestCase extends TestCase {
    
    private static final String TEST_WORDS = 
        "radu\n daru\n tary\n raty\n graty\n oko\n " +
    		"okolic\n okolicy\n okolica\n lica\n lico\n bawi\n wabi\n";
    
    public void testWordsCount() throws Exception {
        Words words = new Words(getClass().getResourceAsStream("words.txt"));
        assertEquals(314, words.getWordsCount());
    }
    
    public void testFindWordsWithPrefix() throws Exception {
        Words words = new Words(new ByteArrayInputStream(TEST_WORDS.getBytes()));
        List<String> matched = words.findStartingWith("oko", null);
        
        assertEquals(4, matched.size());
        assertTrue(matched.contains("oko"));
        assertTrue(matched.contains("okolic"));
        assertTrue(matched.contains("okolica"));
        assertTrue(matched.contains("okolicy"));
    }
    
    public void testFindWordsWithPrefixAndLength() throws Exception {
        Words words = new Words(new ByteArrayInputStream(TEST_WORDS.getBytes()));
        List<String> matched = words.findStartingWith("ok", 6);
        
        assertEquals(1, matched.size());
        assertTrue(matched.contains("okolic"));
    }
    
    public void testFindWords() throws Exception {
        Words words = new Words(new ByteArrayInputStream(TEST_WORDS.getBytes()));
        Set<String> matched = words.findWords("licawby", null);
        
        assertEquals(3, matched.size());
        assertTrue(matched.contains("bawi"));
        assertTrue(matched.contains("lica"));
        assertTrue(matched.contains("wabi"));
    }
    
    public void testFindWordsWithLength() throws Exception {
        Words words = new Words(new ByteArrayInputStream(TEST_WORDS.getBytes()));
        Set<String> matched = words.findWords("okolica", 4);
        
        assertEquals(2, matched.size());
        assertTrue(matched.contains("lica"));
        assertTrue(matched.contains("lico"));
    }
    
    public void testCleanWords() throws Exception {
        Words words = new Words(new ByteArrayInputStream(TEST_WORDS.getBytes()));
        assertEquals(13, words.getWordsCount());
        words.clear();
        assertEquals(0, words.getWordsCount());
    }
}
