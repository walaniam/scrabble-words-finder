package pl.walaniam.srabble.gui.actions;

import pl.walaniam.srabble.Words;

import java.util.EventObject;


public class WordsChangedEvent extends EventObject {
    
    private final Words words;

    public WordsChangedEvent(Object source, Words words) {
        super(source);
        this.words = words;
    }

    public Words getWords() {
        return words;
    }
}
