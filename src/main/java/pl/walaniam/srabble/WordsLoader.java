package pl.walaniam.srabble;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Mariusz
 */
public interface WordsLoader {
    
    String ENCODING = "utf-8"; //"windows-1250";
    
    /**
     * 
     * @param wordsStream must be closed in caller code
     * @param toLowerCase
     * @return
     * @throws IOException
     */
    void loadWords(InputStream wordsStream, WordsConsumer consumer, boolean toLowerCase) throws IOException;

    int getLongestWordLength();
}