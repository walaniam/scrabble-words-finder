package pl.walaniam.srabble.gui.layout.start;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.FileConfig;
import pl.walaniam.srabble.gui.actions.OpenFileAction;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.awt.*;

import static pl.walaniam.srabble.gui.FrameUtils.changeFontSize;
import static pl.walaniam.srabble.gui.actions.ActionListenersFactory.openDictionaryDownloadPageOf;
import static pl.walaniam.srabble.gui.layout.LayoutComponents.constraintsOf;

@Slf4j
public class StartPanel extends JPanel {
    
    public StartPanel(OpenFileAction openFileAction) {
        log.debug("Initializing StartPanel...");
        setLayout(new GridBagLayout());
        infoLabelOf();
        openFileButtonOf(openFileAction);
    }

    private JLabel infoLabelOf() {

        JLabel infoLabel = new JLabel();
        infoLabel.setFont(
                changeFontSize(infoLabel.getFont(), FileConfig.FONT_SIZE_INCREMENT)
        );
        infoLabel.setText(I18N.getMessage("StartPanel.file.not.chosen"));
        infoLabel.setForeground(Color.BLUE.darker());
        infoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoLabel.addMouseListener(openDictionaryDownloadPageOf(StartPanel.this));

        add(infoLabel, constraintsOf(0, 0, 0, 0, 30, 0));

        return infoLabel;
    }

    private JButton openFileButtonOf(OpenFileAction action) {
        JButton button = new JButton(I18N.getMessage("StartPanel.open.file"));
        button.addActionListener(action);
        add(button, constraintsOf(0, 1));
        return button;
    }
}
