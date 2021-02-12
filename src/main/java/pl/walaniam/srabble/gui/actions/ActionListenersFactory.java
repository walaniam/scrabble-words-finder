package pl.walaniam.srabble.gui.actions;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.layout.MainFrame;

import java.awt.*;
import java.awt.event.ActionListener;

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
}
