package overlays;

import helpers.ImageButton;
import helpers.Images;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PlusOverlay extends JPanel {
    private JButton plusButton;
    private JPanel rootPanel;

    public void addActionListener(ActionListener actionListener) {
        plusButton.addActionListener(actionListener);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        //Альтернтивное решение
        plusButton = new ImageButton(Images.getPlus());
    }
}
