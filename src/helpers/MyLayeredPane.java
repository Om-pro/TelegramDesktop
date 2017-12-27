package helpers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HerrSergio on 25.04.2016.
 */
public class MyLayeredPane extends JLayeredPane {

    @Override
    public void doLayout() {
        super.doLayout();
        for(Component component : getComponents()) {
            component.setBounds(0, 0, this.getWidth(), this.getHeight());
        }
    }
}
