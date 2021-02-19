package pl.walaniam.srabble.gui.actions;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.i18n.I18N;
import pl.walaniam.srabble.gui.layout.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class ActionListenersFactory {

    public static ActionListener disposingActionOf(Window w) {
        return actionEvent -> {
            log.debug("Disposing {}", w);
            w.dispose();
        };
    }

    public static OpenFileAction openFileActionOf(MainFrame mainFrame) {
        return new OpenFileAction(mainFrame, fileToOpen -> {
            LoadWordsWorker worker = new LoadWordsWorker(mainFrame, fileToOpen, true);
            worker.startExecution();
        });
    }

    public static MouseListener openDictionaryDownloadPageOf(Component parent) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(I18N.getMessage("StartPanel.file.download.url")));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(
                            parent,
                            String.valueOf(ex),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
    }
}
