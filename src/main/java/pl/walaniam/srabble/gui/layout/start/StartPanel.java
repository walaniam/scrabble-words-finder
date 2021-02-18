package pl.walaniam.srabble.gui.layout.start;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.FileConfig;
import pl.walaniam.srabble.gui.actions.OpenFileAction;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        infoLabel.setForeground(Color.BLUE.darker());
        infoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(I18N.getMessage("StartPanel.file.download.url")));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(
                            StartPanel.this,
                            String.valueOf(ex),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 30, 0);
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
