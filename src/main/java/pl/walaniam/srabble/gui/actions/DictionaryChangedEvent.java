package pl.walaniam.srabble.gui.actions;

import lombok.Getter;
import walaniam.scrabble.dictionary.Dictionary;

import java.util.EventObject;

public class DictionaryChangedEvent extends EventObject {

    @Getter
    private final Dictionary dictionary;

    public DictionaryChangedEvent(Object source, Dictionary dictionary) {
        super(source);
        this.dictionary = dictionary;
    }
}
