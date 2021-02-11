package pl.walaniam.srabble.gui;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.actions.OpenFileAction;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.awt.*;

import static pl.walaniam.srabble.gui.FrameUtils.changeFontSize;

@Slf4j
public class StartPanel extends JPanel {
    
    private final JLabel infoLabel;
    private final JButton button;
    
    public StartPanel(OpenFileAction openFileAction) {
        log.debug("Initializing StartPanel...");
        setLayout(new GridBagLayout());
        this.infoLabel = infoLabelOf();
        this.button = openFileButtonOf(openFileAction);
    }

    private JLabel infoLabelOf() {

        JLabel infoLabel = new JLabel();
        infoLabel.setFont(
                changeFontSize(infoLabel.getFont(), FileConfig.FONT_SIZE_INCREMENT)
        );
        infoLabel.setText(I18N.getMessage("StartPanel.file.not.chosen"));
        infoLabel.setForeground(Color.RED);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        add(infoLabel, c);

        return infoLabel;
    }

    private JButton openFileButtonOf(OpenFileAction action) {

        JButton button = new JButton(I18N.getMessage("StartPanel.open.file"));
        button.addActionListener(action);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(button, c);

        return button;
    }
}
