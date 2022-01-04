package pl.walaniam.srabble.gui;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FrameUtils {
    
    public static void center(Window window) {

        Rectangle bounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        int xLocation = bounds.x + (bounds.width - window.getWidth()) / 2;
        int yLocation = bounds.y + (bounds.height - window.getHeight()) / 2;
        if (xLocation < 0) {
            xLocation = 0;
        }
        if (yLocation < 0) {
            yLocation = 0;
        }
        window.setLocation(xLocation, yLocation);
    }
    
    public static Font changeFontSize(Font font, int increment) {
        return new Font(
                font.getName(),
                Font.PLAIN,
                font.getSize() + increment
        );
    }
    
    /**
     * Shows the confirm message
     * @param parent
     * @param message
     * @return true if confirmed with YES
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        
        int result = JOptionPane.showOptionDialog(
                parent,
                message,
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] { "Tak", "Nie" },
                "Nie"
        );

        return result == JOptionPane.YES_OPTION;
    }

}
