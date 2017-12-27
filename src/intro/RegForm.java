package intro;

import helpers.Helper;
import helpers.HintTextFieldUnderlined;
import helpers.ImagePanel;
import helpers.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Ompro on 01.06.2017.
 */
public class RegForm extends Background {

    private JPanel rootPanel;
    private JTextField surnameField;
    private JTextField nameField;
    private JButton regButton;
    private JPanel logoPanel;
    private JTextPane textPane;

    {
        Helper.centerAlignText(textPane);
        Helper.decorateAsImageButton(regButton, Images.getSendButton(), null, Color.WHITE);
        nameField.setBorder(null);
        surnameField.setBorder(null);
        Helper.clearBoth(nameField);
        Helper.clearBoth(surnameField);
    }

    public void clear() {
        nameField.setText("");
        surnameField.setText("");
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {
        regButton.addActionListener(actionListener);
    }

    public String getSurnameField() {
        return surnameField.getText();
    }

    public String getNameField() {
        return nameField.getText();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        nameField = new HintTextFieldUnderlined("", "Имя", true, false);
        surnameField = new HintTextFieldUnderlined("", "Фамилия", true, false);

        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
    }
}
