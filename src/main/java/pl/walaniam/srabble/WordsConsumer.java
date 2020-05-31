package pl.walaniam.srabble;



public interface WordsConsumer {
    /**
     * Add a word
     * 
     * @param word
     */
    void add(String word);
    
    /**
     * Number of words in this consumer instance
     * 
     * @return
     */
    int size();
}
