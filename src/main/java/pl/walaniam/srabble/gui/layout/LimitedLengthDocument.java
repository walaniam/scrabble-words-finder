package pl.walaniam.srabble.gui.layout;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimitedLengthDocument extends PlainDocument {

    private final int maxLength;

    public LimitedLengthDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (getLength() > (maxLength - 1)) {
            return;
        }
        super.insertString(offs, str, a);
    }
}
