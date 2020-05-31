package pl.walaniam.srabble.gui;

import lombok.extern.slf4j.Slf4j;
import pl.walaniam.srabble.Words;
import pl.walaniam.srabble.gui.i18n.I18N;
import pl.walaniam.srabble.gui.layout.DocumentListeningComboBoxModel;
import pl.walaniam.srabble.gui.layout.LimitedLengthDocument;
import pl.walaniam.srabble.gui.layout.WordsListeningComboBoxModel;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static pl.walaniam.srabble.util.LetterGroupingUtils.groupByLength;

@Slf4j
public class MainPanel extends JPanel {

    private static final int MAX_PERMUTATION_LENGTH = 11;
    
    private final MainFrame mainFrame;
    /////////////// Top panel //////////////////////////////
    private final JPanel topPanel = new JPanel(new GridBagLayout());
    private final JLabel lettersL = new JLabel();
    private final JTextField lettersTF = new JTextField();
    private final JButton searchWordsB = new JButton();
    private final JButton lettersCleanB = new JButton();
    private final JLabel lettersWordLengthL = new JLabel();
    private final JComboBox lettersWordLengthCB = new JComboBox();
    private final JLabel prefixL = new JLabel();
    private final JLabel prefixWordLengthL = new JLabel();
    private final JTextField prefixTF = new JTextField();
    private final JButton prefixSearchB = new JButton();
    private final JComboBox prefixWordLengthCB = new JComboBox();
    private final JButton prefixCleanB = new JButton();
    //////////////////////////////////////////////////////////
    
    private JScrollPane wordsScrollPane;
    private JEditorPane wordsPane;
    private final JPanel bottomPanel = new JPanel(new GridBagLayout());
    private final JLabel bottomLabel = new JLabel();
    
    /**
     * Initialize this panel
     * 
     * @param mainFrame parent
     */
    MainPanel(MainFrame mainFrame) {
        log.debug("Initializing MainPanel...");
        this.mainFrame = mainFrame;
        initComponents();
        layoutComponents();
        log.debug("MainPanel initialized");
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        initTopPanelComponents();
        initWordsPanel();
        initBottomPanel();
    }

    private void layoutComponents() {
        layoutTopPanelComponents();
        add(BorderLayout.NORTH, topPanel);
        add(BorderLayout.CENTER, wordsScrollPane);
        add(BorderLayout.SOUTH, bottomPanel);
    }

    private void initTopPanelComponents() {
        
        final KeyListener keyListener = new TopPanelKeyListener();
        DocumentListeningComboBoxModel lettersLengthsModel = new DocumentListeningComboBoxModel(MAX_PERMUTATION_LENGTH);
        
        lettersL.setText(I18N.getMessage("MainPanel.TopPanel.letters.label"));
        
        lettersTF.setColumns(10);        
        lettersTF.addKeyListener(keyListener);
        final LimitedLengthDocument lettersDocument = new LimitedLengthDocument(MAX_PERMUTATION_LENGTH);
        lettersDocument.addDocumentListener(lettersLengthsModel);
        lettersTF.setDocument(lettersDocument);
        lettersWordLengthCB.setModel(lettersLengthsModel);
        
        searchWordsB.setText(I18N.getMessage("MainPanel.TopPanel.search.button"));
        searchWordsB.addActionListener(e -> handleSearchWords());
        searchWordsB.addKeyListener(keyListener);
        
        lettersCleanB.setText(I18N.getMessage("MainPanel.TopPanel.cancel.button"));
        lettersCleanB.addActionListener(e -> handleLettersClean());
        lettersCleanB.addKeyListener(keyListener);
        
        lettersWordLengthL.setText(I18N.getMessage("MainPanel.TopPanel.word.length"));
     
        prefixL.setText(I18N.getMessage("MainPanel.TopPanel.prefix"));
        
        prefixTF.setColumns(10);
        prefixTF.addKeyListener(keyListener);
        
        prefixCleanB.setText(I18N.getMessage("MainPanel.TopPanel.cancel.button"));
        prefixCleanB.addKeyListener(keyListener);
        prefixCleanB.addActionListener(e -> handlePrefixClean());
        
        prefixWordLengthL.setText(I18N.getMessage("MainPanel.TopPanel.word.length"));
        
        WordsListeningComboBoxModel prefixModel = new WordsListeningComboBoxModel(0);
        this.mainFrame.addWordsListener(prefixModel);
        prefixWordLengthCB.setModel(prefixModel);
        
        prefixSearchB.setText(I18N.getMessage("MainPanel.TopPanel.search.button"));
        prefixSearchB.addKeyListener(keyListener);
        prefixSearchB.addActionListener(e -> handlePrefixSearch());
    }
    
    public void handleClean() {
        handlePrefixClean();
        handleLettersClean();
    }
    
    private void handleLettersClean() {
        lettersTF.setText("");
        wordsPane.setText("");
        lettersWordLengthCB.setSelectedIndex(0);
    }
    
    private void handlePrefixClean() {
        prefixTF.setText(""); 
        wordsPane.setText("");
        prefixWordLengthCB.setSelectedIndex(0);
    }
    
    private void handlePrefixSearch() {
        String prefix = prefixTF.getText();
        if (canSearch(prefix, 1)) {
            prefix = prefix.trim().toLowerCase();
            final Integer length = getSelectedInteger(prefixWordLengthCB.getSelectedItem());
            final Words words = mainFrame.getWords();
            try {
                mainFrame.setBusy(true);
                List<String> foundWords = words.findStartingWith(prefix, length);
                Map<Integer, List<String>> grouped = groupByLength(foundWords, Comparable::compareTo);
                mainFrame.setBusy(false);
                printSearchResult(grouped, mainFrame.getWords().getLongestWordLength());
            } finally {
                mainFrame.setBusy(false);
            }
        }
    }
    
    private Integer getSelectedInteger(Object selected) {
        Integer result = null;
        if (selected instanceof Integer) {
            result = (Integer) selected;
        } else if (selected != null) {
            try {
                result = new Integer(String.valueOf(selected));
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
    
    private void handleSearchWords() {
        String lettersTxt = lettersTF.getText();
        if (canSearch(lettersTxt, 0)) {
            lettersTxt = lettersTxt.trim().toLowerCase();
            int longest = lettersTxt.length();
            Integer length = getSelectedInteger(lettersWordLengthCB.getSelectedItem());
            Words words = mainFrame.getWords();
            try {
                mainFrame.setBusy(true);
                Set<String> foundWords = words.findWords(lettersTxt, length);
                Map<Integer, List<String>> grouped = groupByLength(foundWords, Comparable::compareTo);
                mainFrame.setBusy(false);
                printSearchResult(grouped, longest);
            } finally {
                mainFrame.setBusy(false);
            }
        }
    }

    private void printSearchResult(Map<Integer, List<String>> grouped, int longest) {
        
        final long startTime = System.currentTimeMillis();
        
        final StringBuilder sb = new StringBuilder();
        for (int i = longest; i > 0; i--) {
            final List<String> nLetterWords = grouped.get(i);
            if (nLetterWords != null) {
                sb.append("<b>");
                sb.append(I18N.getMessage("MainPanel.WordsPanel.n.char.words", i));
                sb.append("</b><br>");

                for (Iterator iter = nLetterWords.iterator(); iter.hasNext();) {
                    sb.append(iter.next());
                    if (iter.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append("<br><br>");
            }
        }
        
        final String content = sb.toString();
        
        wordsPane.setText(content);
        sb.delete(0, sb.length());
        
        if (log.isDebugEnabled()) {
            log.debug("Result printed in {} ms", System.currentTimeMillis() - startTime);
            log.debug("Found words:\n" + content);
        }
    }
    
    private boolean canSearch(String lettersTxt, int minLength) {
        return lettersTxt != null && lettersTxt.trim().length() > minLength;
    }    

    private void layoutTopPanelComponents() {
        
        topPanel.setLayout(new GridBagLayout());
        
        final int sideMargin = 20;

        // ========== row 1 ============
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, sideMargin, 3, 3);
        topPanel.add(lettersL, c);
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 3, 3, 3);
        topPanel.add(lettersTF, c);
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 0, 3, 3);
        topPanel.add(searchWordsB, c);
        
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(5, 3, 3, sideMargin);
        topPanel.add(lettersCleanB, c);
        
        // ========== row 2 ============
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.insets = new Insets(3, sideMargin, 10, 3);
        topPanel.add(lettersWordLengthL, c);
     
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 3, 10, 6);
        topPanel.add(lettersWordLengthCB, c);          
        
        // ============ row 3 ============
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 5;
        c.weightx = 1;
        //c.insets = new Insets(3, 15, 3, 15);
        c.anchor = GridBagConstraints.CENTER;
        JSeparator sp = new JSeparator(JSeparator.HORIZONTAL);
        sp.setPreferredSize(new Dimension(380, 2));
        topPanel.add(sp, c);
        
        // ============ row 4 ============
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.insets = new Insets(10, sideMargin, 3, 3);
        topPanel.add(prefixL, c);
     
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 3, 3, 3);
        topPanel.add(prefixTF, c);        
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(10, 0, 3, 3);
        topPanel.add(prefixSearchB, c);
        
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(10, 3, 3, sideMargin);
        topPanel.add(prefixCleanB, c);        
        
        // ============ row 5 ============
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.insets = new Insets(3, sideMargin, 10, 3);
        topPanel.add(prefixWordLengthL, c);
     
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 3, 10, 3);
        topPanel.add(prefixWordLengthCB, c);        
    }
    
    private void initWordsPanel() {
        wordsPane = new JEditorPane();        
        wordsPane.setEditable(false);
        wordsPane.setEditorKit(new HTMLEditorKit());
        
        wordsScrollPane = new JScrollPane(wordsPane);
        wordsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        wordsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private void initBottomPanel() {
        bottomPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 15);
        
        bottomLabel.setText("");
        bottomPanel.add(bottomLabel, c);
    }
    
    public void setFooterText(String text) {
        bottomLabel.setText(text);        
    }
    
    protected void setBusy(boolean busy) {
        lettersTF.setEnabled(!busy);
        lettersCleanB.setEnabled(!busy);
        lettersWordLengthCB.setEnabled(!busy);
        searchWordsB.setEnabled(!busy);
        prefixSearchB.setEnabled(!busy);
        lettersCleanB.setEnabled(!busy);
        prefixCleanB.setEnabled(!busy);
        prefixTF.setEnabled(!busy);
        prefixWordLengthCB.setEnabled(!busy);
    }

    private class TopPanelKeyListener implements KeyListener {
        
        public void keyPressed(KeyEvent e) {
            final Object source = e.getSource();
            if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                if (source == lettersTF || source == searchWordsB) {
                    handleSearchWords();
                } else if (source == lettersCleanB) {
                    handleLettersClean();
                } else if (source == prefixTF || source == prefixSearchB) {
                    handlePrefixSearch();
                } else if (source == prefixCleanB) {
                    handlePrefixClean();
                }
            } else if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
                handleClean();
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }

}
