package pl.walaniam.srabble.gui.actions;

import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingworker.SwingWorker;
import pl.walaniam.srabble.Words;
import pl.walaniam.srabble.gui.Configuration;
import pl.walaniam.srabble.gui.MainFrame;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

@Slf4j
public class LoadWordsWorker extends SwingWorker<Words, Object> {

    private final File fileToOpen;
    private final MainFrame mainFrame;
    private final boolean saveConfig;
    
    public LoadWordsWorker(MainFrame frame, File fileToOpen) {
        this(frame, fileToOpen, true);
    }
    
    public LoadWordsWorker(MainFrame frame, File fileToOpen, boolean saveConfig) {
        this.fileToOpen = fileToOpen;
        this.mainFrame = frame;
        this.saveConfig = saveConfig;
    }

    @Override
    protected Words doInBackground() throws Exception {
        
        if (log.isDebugEnabled()) {
            log.debug("START doInBackground");
            log.debug("File is: " + fileToOpen.getAbsolutePath());
        }
        
        final Words words = new Words(fileToOpen);
        
        log.debug("END doInBackground");

        return words;
    }
    
    @Override
    protected void done() {
        try {
            mainFrame.getMainPanel().setFooterText(
                    I18N.getMessage("WordsLoaderThread.opening.progress"));
            mainFrame.invalidate();

            final Words words = get();
            mainFrame.setWords(words);

            final JPanel currentPanel = mainFrame.getCurrentPanel();
            if (currentPanel != mainFrame.getMainPanel()) {
                mainFrame.setCurrentPanel(mainFrame.getMainPanel());
            }

            if (saveConfig) {
                Configuration.getInstance().setProperty(Configuration.DICTIONARY_FILE_PATH,
                        fileToOpen.getAbsolutePath());
                Configuration.getInstance().save();
            }

            final String footerText = I18N.getMessage("WordsLoaderThread.words.num", NumberFormat
                    .getIntegerInstance(new Locale("pl")).format(words.getWordsCount()));
            mainFrame.getMainPanel().setFooterText(footerText);
            mainFrame.getMainPanel().handleClean();

        } catch (Exception e) {
            log.error("Exception while opening " + fileToOpen, e);
        } finally {
            mainFrame.setBusy(false);
            System.gc();
        }
    }
    
    public void startExecution() {
        mainFrame.setBusy(true);        
        execute();
    }
    
}
