package intro;

import helpers.Helper;
import helpers.ImagePanel;
import helpers.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * Created by Ompro on 01.06.2017.
 */
public class MobileNumberForm extends Background {

    private JPanel rootPanel;
    private JFormattedTextField numberTextField;
    private JButton regButton;
    private JPanel logoPanel;
    private JTextPane textPane;
    private JPanel phoneIconPanel;
    private final static String INDEX = "+7";

    {
        Helper.centerAlignText(textPane);
        numberTextField.setBorder(null);
        Helper.clearBoth(numberTextField);
    }

    public MobileNumberForm() {
        try {
            numberTextField.setFormatterFactory(new DefaultFormatterFactory(getMaskFormatter()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Helper.decorateAsImageButton(regButton, Images.getSendButton(), null, Color.WHITE);
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {

        regButton.addActionListener(actionListener);
    }

    public void transferFocusTo() {
        numberTextField.requestFocusInWindow();
    }

    public String getFormattedValue() {

        try {
            numberTextField.commitEdit();

        } catch (ParseException pe) {
            return null;
        }

        return (String) numberTextField.getValue();
    }

    public MaskFormatter getMaskFormatter() throws ParseException {

        MaskFormatter maskFormatter = new MaskFormatter(INDEX + "(###)###-####");
        maskFormatter.setValidCharacters("0123456789");
        maskFormatter.setPlaceholderCharacter('_');

        return maskFormatter;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        logoPanel = new ImagePanel(Images.getLogo(), false, true, 0);
        phoneIconPanel = new ImagePanel(Images.getPhoneIcon(), false, true, 0);
    }

    public void clear() {
        numberTextField.setText("");
        numberTextField.setValue("");
    }
}
