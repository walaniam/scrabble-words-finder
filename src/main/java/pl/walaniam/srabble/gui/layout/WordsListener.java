package pl.walaniam.srabble.gui.layout;

import pl.walaniam.srabble.gui.actions.DictionaryChangedEvent;

public interface WordsListener {

    /**
     * Fired when Words object stored by MainFrame has been changed.
     * 
     * @param e
     */
    void dictionaryChanged(DictionaryChangedEvent e);
}
