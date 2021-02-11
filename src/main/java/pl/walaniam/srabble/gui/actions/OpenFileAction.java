package pl.walaniam.srabble.gui.actions;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.gui.i18n.I18N;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Consumer;

@Slf4j
public class OpenFileAction implements ActionListener {

    private final Component parent;
    private final JFileChooser fileChooser;
    private final FileFilter filter;
    private final Consumer<File> fileConsumer;

    public OpenFileAction(Component parent, Consumer<File> fileConsumer) {
        this.parent = parent;
        this.fileConsumer = fileConsumer;
        filter = createTextFileFilter();
        fileChooser = createFileChooser();
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle(I18N.getMessage("OpenFileAction.choose.file"));
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser;
    }

    private FileFilter createTextFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.exists() && ((f.isFile() && f.getName().endsWith(".txt")) || f.isDirectory());
            }

            @Override
            public String getDescription() {
                return I18N.getMessage("OpenFileAction.txt.file");
            }
        };
    }
    
    public void actionPerformed(ActionEvent event) {
        
        int status = fileChooser.showOpenDialog(parent);
        File fileToOpen = fileChooser.getSelectedFile();

        log.debug("Opening file {}", fileToOpen);

        if (status == JFileChooser.APPROVE_OPTION && fileToOpen != null && filter.accept(fileToOpen)) {
            fileConsumer.accept(fileToOpen);
        }
    }

}
