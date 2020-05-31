package pl.walaniam.srabble.gui.layout;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Changes its size when {@link DocumentListener} events occur
 */
public class DocumentListeningComboBoxModel extends DynamicComboBoxModel implements DocumentListener {

    public DocumentListeningComboBoxModel(int numbers) {
        super(numbers);
    }

    public void changedUpdate(DocumentEvent e) {
        populateModel(e.getDocument().getLength());
    }

    public void insertUpdate(DocumentEvent e) {
        populateModel(e.getDocument().getLength());
    }

    public void removeUpdate(DocumentEvent e) {
        populateModel(e.getDocument().getLength());
    }
}
