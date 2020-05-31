package pl.walaniam.srabble.gui.layout;

import pl.walaniam.srabble.gui.actions.WordsChangedEvent;
import pl.walaniam.srabble.gui.actions.WordsListener;

public class WordsListeningComboBoxModel extends DynamicComboBoxModel implements WordsListener {

    public WordsListeningComboBoxModel(int numbers) {
        super(numbers);
    }

    public void wordsChanged(WordsChangedEvent e) {
        int size = e.getWords() == null ? 0 : e.getWords().getLongestWordLength();
        populateModel(size);
    }
}
