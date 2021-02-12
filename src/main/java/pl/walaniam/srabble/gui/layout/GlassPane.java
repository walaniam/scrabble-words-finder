package pl.walaniam.srabble.gui.layout;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

public class GlassPane extends JPanel {

    private static final long serialVersionUID = 1L;

    public GlassPane() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });
    }
}
