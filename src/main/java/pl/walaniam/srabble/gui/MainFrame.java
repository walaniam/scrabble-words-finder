package pl.walaniam.srabble.gui;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import pl.walaniam.srabble.Words;
import pl.walaniam.srabble.gui.actions.LoadWordsWorker;
import pl.walaniam.srabble.gui.actions.OpenFileAction;
import pl.walaniam.srabble.gui.actions.WordsChangedEvent;
import pl.walaniam.srabble.gui.actions.WordsListener;
import pl.walaniam.srabble.gui.i18n.I18N;
import pl.walaniam.srabble.gui.laf.UICustomizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Component
@Slf4j
public class MainFrame extends JFrame implements InitializingBean {

    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 450;
    private static final int FRAME_HEIGHT = 580;

    private final Set<WordsListener> wordsListeners = new LinkedHashSet<>();
    @Getter
    private final FileConfig fileConfig;
    @Getter
    private JPanel currentPanel;
    @Getter
    private MainPanel mainPanel;
    private StartPanel startPanel;
    private JMenuBar menuBar;
    @Getter
    private Words words;
    private volatile boolean frameIsBusy = false;

    @Autowired
    public MainFrame(GlassPane glassPane, FileConfig fileConfig) {
        log.debug("Initializing MainFrame...");
        this.fileConfig = fileConfig;

        customizeI18N();
        setLAF();

        setGlassPane(glassPane);

        initMenu();
        initFrameComponents();

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(I18N.getMessage("MainFrame.title"));
        setResizable(false);

        FrameUtils.center(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        log.debug("MainFrame initialized");
    }

    @Override
    public void afterPropertiesSet() {
        setVisible(true);
        invalidate();
        startWordsLoadingWorker();
    }

    private void customizeI18N() {
        Map<String, String> messages = I18N.getMessages("java.", true);
        messages.entrySet().forEach(entry -> UIManager.put(entry.getKey(), entry.getValue()));
    }

    private void setLAF() {
        String os = System.getProperty("os.name");
        try {
            String laf = (os.toLowerCase().indexOf("windows") > -1)
                    ? "com.jgoodies.looks.windows.WindowsLookAndFeel"
                    : "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            log.debug("Setting LAF to {} on OS {}", laf, os);
            UIManager.setLookAndFeel(laf);
        } catch (Exception e) {
            log.error("Cannot set required L&F", e);
            UICustomizer.setCustomizedLAF();
        }
    }

    private void initMenu() {
        JMenu fileMenu = new JMenu(I18N.getMessage("MainFrame.menu.file"));
        JMenuItem openFile = new JMenuItem(I18N.getMessage("MainFrame.menu.file.open"));
        openFile.addActionListener(new OpenFileAction(this));
        fileMenu.add(openFile);
        fileMenu.addSeparator();

        JMenuItem close = new JMenuItem(I18N.getMessage("MainFrame.menu.file.close"));
        close.addActionListener(e -> {
            log.debug("Disposing MainFrame...");
            MainFrame.this.dispose();
        });
        fileMenu.add(close);

        JMenu helpMenu = new JMenu(I18N.getMessage("MainFrame.menu.help"));
        JMenuItem about = new JMenuItem(I18N.getMessage("MainFrame.menu.help.about"));
        about.addActionListener(e -> {
            JDialog aboutDialog = new JDialog(MainFrame.this);
            aboutDialog.setModal(true);
            aboutDialog.setSize(250, 150);
            aboutDialog.setPreferredSize(new Dimension(250, 150));
            aboutDialog.setResizable(false);
            aboutDialog.setTitle(I18N.getMessage("MainFrame.menu.help.about"));
            aboutDialog.add(new JLabel("  Program dla ulatwienia gry w scrabble"));
            FrameUtils.center(aboutDialog);
            aboutDialog.setVisible(true);
        });
        helpMenu.add(about);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void initFrameComponents() {

        startPanel = new StartPanel(this);
        mainPanel = new MainPanel(this);

        final String lastOpenedFile = fileConfig.getProperty(FileConfig.DICTIONARY_FILE_PATH);

        JPanel panelToAdd = startPanel;
        if (lastOpenedFile != null) {
            File lastOpenedFilePath = new File(lastOpenedFile);
            if (lastOpenedFilePath.exists() && lastOpenedFilePath.isFile()) {
                panelToAdd = mainPanel;
            }
        }

        setCurrentPanel(panelToAdd);
    }

    private void startWordsLoadingWorker() {
        String lastOpenedFile = fileConfig.getProperty(FileConfig.DICTIONARY_FILE_PATH);
        if (lastOpenedFile != null) {
            File lastOpenedFilePath = new File(lastOpenedFile);
            if (lastOpenedFilePath.exists() && lastOpenedFilePath.isFile()) {
                LoadWordsWorker worker = new LoadWordsWorker(this, lastOpenedFilePath, false);
                worker.startExecution();
            }
        }
    }

    public void setCurrentPanel(JPanel currentPanel) {
        if (this.currentPanel != null) {
            getContentPane().remove(this.currentPanel);
        }
        this.currentPanel = currentPanel;
        getContentPane().add(this.currentPanel);
    }

    public synchronized void setBusy(final boolean busy) {
        if (this.frameIsBusy == busy) {
            return;
        } else {
            this.frameIsBusy = busy;

            Component gp = getGlassPane();
            if (gp != null) {
                Cursor cursor = Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR);
                setCursor(cursor);
            }

            getMainPanel().setBusy(busy);
            enableMenuElements(new MenuElement[]{menuBar}, !busy);
        }
    }

    private void enableMenuElements(MenuElement[] menuItems, final boolean enable) {
        if (menuItems != null) {
            for (MenuElement elem : menuItems) {
                if (elem instanceof JMenuItem) {
                    ((JMenuItem) elem).setEnabled(enable);
                } else {
                    enableMenuElements(elem.getSubElements(), enable);
                }
            }
        }
    }

    public void setWords(Words words) {
        this.words = words;
        fireWordsChangedEvent(new WordsChangedEvent(MainFrame.this, words));
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        boolean exitEvent = false;
        boolean confirmed = false;
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            exitEvent = true;
            confirmed = FrameUtils.showConfirmDialog(this, I18N.getMessage("MainFrame.menu.confirm.exit"));
        }
        if (!exitEvent || (exitEvent && confirmed)) {
            super.processWindowEvent(e);
        }
    }

    public void addWordsListener(WordsListener l) {
        wordsListeners.add(l);
    }

    private void fireWordsChangedEvent(WordsChangedEvent e) {
        log.debug("Fired WordsChangedEvent...");
        wordsListeners.forEach(it -> it.wordsChanged(e));
    }
}
