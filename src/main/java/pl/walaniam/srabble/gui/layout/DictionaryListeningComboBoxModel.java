package pl.walaniam.srabble.gui.layout;

import pl.walaniam.srabble.gui.actions.DictionaryChangedEvent;
import walaniam.scrabble.dictionary.Dictionary;

import java.util.Optional;

public class DictionaryListeningComboBoxModel extends DynamicComboBoxModel implements WordsListener {

    public DictionaryListeningComboBoxModel(int numbers) {
        super(numbers);
    }

    public void dictionaryChanged(DictionaryChangedEvent e) {
        int size = Optional.ofNullable(e.getDictionary())
                .map(Dictionary::longestWordLength)
                .orElse(0);
        populateModel(size);
    }
}
