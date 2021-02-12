package pl.walaniam.srabble.gui.actions;

import lombok.Getter;
import pl.walaniam.srabble.model.Words;

import java.util.EventObject;


public class WordsChangedEvent extends EventObject {

    @Getter
    private final Words words;

    public WordsChangedEvent(Object source, Words words) {
        super(source);
        this.words = words;
    }
}
