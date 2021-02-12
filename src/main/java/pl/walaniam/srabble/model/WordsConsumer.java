package pl.walaniam.srabble.model;

public interface WordsConsumer {

    void add(String word);
    
    int size();
}
