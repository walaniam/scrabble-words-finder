package pl.walaniam.srabble.gui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

@Component
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
