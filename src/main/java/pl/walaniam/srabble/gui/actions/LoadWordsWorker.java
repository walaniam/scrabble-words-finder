package pl.walaniam.srabble.gui.actions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.FileConfig;
import pl.walaniam.srabble.gui.i18n.I18N;
import pl.walaniam.srabble.gui.layout.MainFrame;
import walaniam.scrabble.dictionary.Dictionary;
import walaniam.scrabble.dictionary.Words;
import walaniam.scrabble.dictionary.set.HashSetWords;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.Locale;

@AllArgsConstructor
@Slf4j
public class LoadWordsWorker extends SwingWorker<Dictionary, Object> {

    private final MainFrame mainFrame;
    private final File fileToOpen;
    private final boolean saveConfig;

    @Override
    protected Dictionary doInBackground() throws Exception {
        log.debug("START doInBackground for file {}", fileToOpen);
        try (FileInputStream fis = new FileInputStream(fileToOpen)) {
            Words words = HashSetWords.open(fis);
            Dictionary dictionary = new Dictionary(words);
            log.debug("END doInBackground");
            return dictionary;
        }
    }

    @Override
    protected void done() {
        try {
            mainFrame.getMainPanel().setFooterText(I18N.getMessage("WordsLoaderThread.opening.progress"));
            mainFrame.invalidate();

            final Dictionary dictionary = get();
            mainFrame.setDictionary(dictionary);

            final JPanel currentPanel = mainFrame.getCurrentPanel();
            if (currentPanel != mainFrame.getMainPanel()) {
                mainFrame.setCurrentPanel(mainFrame.getMainPanel());
            }

            if (saveConfig) {
                FileConfig fileConfig = mainFrame.getFileConfig();
                fileConfig.setProperty(FileConfig.DICTIONARY_FILE_PATH, fileToOpen.getAbsolutePath());
                fileConfig.save();
            }

            String footerText = I18N.getMessage("WordsLoaderThread.words.num", NumberFormat
                    .getIntegerInstance(new Locale("pl")).format(dictionary.totalWords()));
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
