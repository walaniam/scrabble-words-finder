package pl.walaniam.srabble.gui.layout;

import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;

/**
 * Dynamic combo box model which can dynamically change its size
 */
class DynamicComboBoxModel extends DefaultComboBoxModel {

    private volatile int currentSize;

    public DynamicComboBoxModel(int numbers) {
        populateModel(numbers);
    }

    protected synchronized void populateModel(int numbers) {

        if (currentSize != numbers) {

            Object selectedItem = getSelectedItem();

            removeAllElements();
            addElement(I18N.getMessage("MainPanel.TopPanel.prefixWordLengthCB.any"));
            for (int i = 1; i <= numbers; i++) {
                addElement(i);
            }
            currentSize = numbers;

            if (selectedItem != null && getIndexOf(selectedItem) > -1) {
                setSelectedItem(selectedItem);
            }
        }
    }
}
