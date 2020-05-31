package pl.walaniam.srabble;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Mariusz
 */
public interface WordsLoader {
    
    String ENCODING = "windows-1250";
    
    /**
     * 
     * @param wordsStream must be closed in caller code
     * @param toLowerCase
     * @return
     * @throws IOException
     */
    void loadWords(final InputStream wordsStream, WordsConsumer consumer, 
            boolean toLowerCase) throws IOException;

    /**
     * 
     * @return
     */
    int getLongestWordLength();
}