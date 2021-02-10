package pl.walaniam.srabble.gui;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.actions.OpenFileAction;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.awt.*;

import static pl.walaniam.srabble.gui.FrameUtils.changeFontSize;

@Slf4j
public class StartPanel extends JPanel {
    
    private JLabel infoLabel = new JLabel();
    private JButton button;
    
    StartPanel(MainFrame mainFrame) {
        
        log.debug("Initializing StartPanel...");

        setLayout(new GridBagLayout());
        initComponents(mainFrame);
        layoutComponents();
        
        log.debug("Initialized StartPanel");
    }
    
    private void initComponents(MainFrame mainFrame) {
        
        Font newFont = changeFontSize(infoLabel.getFont(), FileConfig.FONT_SIZE_INCREMENT);
        infoLabel.setFont(newFont);
        infoLabel.setText(I18N.getMessage("StartPanel.file.not.chosen"));
        infoLabel.setForeground(Color.RED);
        
        button = new JButton(I18N.getMessage("StartPanel.open.file"));
        button.addActionListener(new OpenFileAction(mainFrame));
    }
    
    private void layoutComponents() {
        GridBagConstraints c = new GridBagConstraints();      
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        add(infoLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        add(button, c);        
    }
}
