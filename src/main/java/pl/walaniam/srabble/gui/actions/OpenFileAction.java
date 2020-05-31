package pl.walaniam.srabble.gui.actions;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.MainFrame;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Slf4j
public class OpenFileAction implements ActionListener {
    
    /**
     * Words file choose
     */
    private final JFileChooser fileChooser;

    /**
     * Text file filter
     */
    private final FileFilter filter;

    /**
     * Main frame instance
     */
    private final MainFrame mainFrame;

    /**
     * 
     * @param mainFrame
     */
    public OpenFileAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        filter = createTextFileFilter();
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle(I18N.getMessage("OpenFileAction.choose.file"));
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private FileFilter createTextFileFilter() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.exists()
                        && ((f.isFile() && f.getName().endsWith(".txt")) || f
                                .isDirectory());
            }

            @Override
            public String getDescription() {
                return I18N.getMessage("OpenFileAction.txt.file");
            }
        };
        return filter;
    }
    
    public void actionPerformed(ActionEvent event) {
        
        log.debug("OpenFileAction performed...");

        final int status = fileChooser.showOpenDialog(mainFrame);
        final File fileToOpen = fileChooser.getSelectedFile();

        if (status == JFileChooser.APPROVE_OPTION && fileToOpen != null
                && filter.accept(fileToOpen)) {
            
            final LoadWordsWorker worker = new LoadWordsWorker(mainFrame, fileToOpen);
            worker.startExecution();
        }
    }

}
