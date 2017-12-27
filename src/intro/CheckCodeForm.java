package intro;

import helpers.Helper;
import helpers.ImagePanel;
import helpers.Images;
import helpers.MaxLengthDocumentFilter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Ompro on 01.06.2017.
 */
public class CheckCodeForm extends Background {
    private JPanel rootPanel;
    private JPasswordField passwordField;
    private JButton regButton;
    private JPanel logoPanel;
    private JTextPane textPane;
    private JLabel numberLabel;
    private JPanel lockIconPanel;
    private JPanel codePanel;

    public CheckCodeForm(){
        Helper.centerAlignText(textPane);
        Helper.decorateAsImageButton(regButton, Images.getSendButton(), null, Color.WHITE);
        Helper.clearBoth(passwordField);
        passwordField.setBorder(null);

        if(passwordField.getDocument() instanceof AbstractDocument)
            ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new MaxLengthDocumentFilter(5));
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {
        regButton.addActionListener(actionListener);
    }

    public void transferFocusTo() {
        passwordField.requestFocusInWindow();
    }

    public void setPhoneLabelText(String text) {
        numberLabel.setText(text);
    }

    public char[] getPasswordField() {
        return passwordField.getPassword();
    }

    public void clear() {
        passwordField.setText("");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
        lockIconPanel = new ImagePanel(Images.getLockIcon(), false, true, 0);
    }
}
