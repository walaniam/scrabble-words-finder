package pl.walaniam.srabble.gui.actions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingworker.SwingWorker;
import pl.walaniam.srabble.Words;
import pl.walaniam.srabble.gui.FileConfig;
import pl.walaniam.srabble.gui.MainFrame;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

@AllArgsConstructor
@Slf4j
public class LoadWordsWorker extends SwingWorker<Words, Object> {

    private final MainFrame mainFrame;
    private final File fileToOpen;
    private final boolean saveConfig;

    @Override
    protected Words doInBackground() throws Exception {
        log.debug("START doInBackground for file {}", fileToOpen);
        Words words = new Words(fileToOpen);
        log.debug("END doInBackground");
        return words;
    }

    @Override
    protected void done() {
        try {
            mainFrame.getMainPanel().setFooterText(I18N.getMessage("WordsLoaderThread.opening.progress"));
            mainFrame.invalidate();

            final Words words = get();
            mainFrame.setWords(words);

            final JPanel currentPanel = mainFrame.getCurrentPanel();
            if (currentPanel != mainFrame.getMainPanel()) {
                mainFrame.setCurrentPanel(mainFrame.getMainPanel());
            }

            if (saveConfig) {
                FileConfig fileConfig = mainFrame.getFileConfig();
                ;
                fileConfig.setProperty(FileConfig.DICTIONARY_FILE_PATH, fileToOpen.getAbsolutePath());
                fileConfig.save();
            }

            String footerText = I18N.getMessage("WordsLoaderThread.words.num", NumberFormat
                    .getIntegerInstance(new Locale("pl")).format(words.getWordsCount()));
            mainFrame.getMainPanel().setFooterText(footerText);
            mainFrame.getMainPanel().handleClean();

        } catch (Exception e) {
            log.error("Exception while opening " + fileToOpen, e);
        } finally {
            mainFrame.setBusy(false);
        }
    }

    public void startExecution() {
        mainFrame.setBusy(true);
        execute();
    }

}
