package pl.walaniam.srabble.gui.laf;


import com.jgoodies.looks.plastic.theme.SkyBluer;

import javax.swing.plaf.ColorUIResource;

public class DefaultTheme extends SkyBluer {

    protected ColorUIResource getPrimary1() {
        return new ColorUIResource(10, 36, 106);
    }

    protected ColorUIResource getPrimary2() {
        return new ColorUIResource(91, 135, 206);
    }

    protected ColorUIResource getPrimary3() {
        return new ColorUIResource(166, 202, 240);
    }

    protected ColorUIResource getSecondary1() {
        return new ColorUIResource(102, 102, 102);
    }

    protected ColorUIResource getSecondary2() {
        return new ColorUIResource(153, 153, 153);
    }

    protected ColorUIResource getSecondary3() {
        return new ColorUIResource(214, 214, 214);
    }

}
