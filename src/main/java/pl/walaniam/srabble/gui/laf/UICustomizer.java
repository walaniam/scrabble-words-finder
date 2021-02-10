package pl.walaniam.srabble.gui.laf;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;

@Slf4j
public class UICustomizer {

    public static void setCustomizedLAF() {
        
        if (log.isDebugEnabled()) {
            log.debug("Setting custom Look and Feel to " + PlasticXPLookAndFeel.class.getName());
        }
        
        PlasticXPLookAndFeel.setPlasticTheme(new DefaultTheme());
        PlasticXPLookAndFeel.setTabStyle(PlasticXPLookAndFeel.TAB_STYLE_METAL_VALUE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            log.warn("Exception while setting Look and Feel", e);
        }
        setFonts();
        setColors();
    }

    private static void setFonts() {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.getDefaults().get(key);
            if (value instanceof FontUIResource) {
                FontUIResource font = (FontUIResource) value;
                UIManager.put(key, new FontUIResource(font.getName(), font
                        .getStyle(), 11));
            }
        }
    }

    private static void setColors() {
        UIManager.put("Table.selectionBackground", PlasticXPLookAndFeel
                .getPrimaryControlShadow());
        UIManager.put("Table.gridColor", PlasticXPLookAndFeel
                .getControlShadow());
        UIManager.put("mainPanel", PlasticXPLookAndFeel.getControlShadow());

        UIManager.put("OptionPane.errorDialog.border.background",
                PlasticXPLookAndFeel.getPrimaryControlDarkShadow());
        UIManager.put("OptionPane.errorDialog.titlePane.background",
                PlasticXPLookAndFeel.getPrimaryControl());
        UIManager.put("OptionPane.errorDialog.titlePane.foreground",
                PlasticXPLookAndFeel.getBlack());
        UIManager.put("OptionPane.errorDialog.titlePane.shadow",
                PlasticXPLookAndFeel.getPrimaryControlShadow());

        UIManager.put("OptionPane.warningDialog.border.background",
                PlasticXPLookAndFeel.getPrimaryControlDarkShadow());
        UIManager.put("OptionPane.warningDialog.titlePane.background",
                PlasticXPLookAndFeel.getPrimaryControl());
        UIManager.put("OptionPane.warningDialog.titlePane.foreground",
                PlasticXPLookAndFeel.getBlack());
        UIManager.put("OptionPane.warningDialog.titlePane.shadow",
                PlasticXPLookAndFeel.getPrimaryControlShadow());

        UIManager.put("OptionPane.questionDialog.border.background",
                PlasticXPLookAndFeel.getPrimaryControlDarkShadow());
        UIManager.put("OptionPane.questionDialog.titlePane.background",
                PlasticXPLookAndFeel.getPrimaryControl());
        UIManager.put("OptionPane.questionDialog.titlePane.foreground",
                PlasticXPLookAndFeel.getBlack());
        UIManager.put("OptionPane.questionDialog.titlePane.shadow",
                PlasticXPLookAndFeel.getPrimaryControlShadow());
    }
}
