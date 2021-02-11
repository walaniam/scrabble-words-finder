package pl.walaniam.srabble.gui.layout;

import pl.walaniam.srabble.Words;
import pl.walaniam.srabble.gui.actions.WordsChangedEvent;

import java.util.Optional;

public class WordsListeningComboBoxModel extends DynamicComboBoxModel implements WordsListener {

    public WordsListeningComboBoxModel(int numbers) {
        super(numbers);
    }

    public void wordsChanged(WordsChangedEvent e) {
        int size = Optional.ofNullable(e.getWords())
                .map(Words::getLongestWordLength)
                .orElse(0);
        populateModel(size);
    }
}
