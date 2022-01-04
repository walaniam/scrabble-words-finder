package pl.walaniam.srabble.gui.layout;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LayoutComponents {

    public static GridBagConstraints constraintsOf(int gridX, int gridY) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridX;
        c.gridy = gridY;
        return c;
    }

    public static GridBagConstraints constraintsOf(int gridX, int gridY, int insetTop, int insetLeft,
                                                   int insetBottom, int insetRight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridX;
        c.gridy = gridY;
        c.insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
        return c;
    }
}
