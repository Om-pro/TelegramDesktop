package overlays;

import helpers.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import java.awt.event.ActionListener;

public class AddContactForm extends OverlayBackground {

    private JPanel rootPanel;
    private JPanel photoPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField phoneTextField;
    private JButton closeButton;
    private JButton addButton;

    {
        setContactInfo(new ContactInfo());
        Document document = phoneTextField.getDocument();
        if (document instanceof AbstractDocument)
            ((AbstractDocument) document).setDocumentFilter(new PhoneNumberDocumentFilter());

        ((HintTextField) firstNameTextField).setHintAlignment(JTextField.CENTER);
        ((HintTextField) lastNameTextField).setHintAlignment(JTextField.CENTER);
        ((HintTextField) phoneTextField).setHintAlignment(JTextField.CENTER);

        GuiHelper.decorateAsImageButton(closeButton, Images.getCloseOverlay());
        GuiHelper.decorateAsImageButton(addButton, Images.getAddContact());

        Helper.clearBoth(firstNameTextField);
        Helper.clearBoth(lastNameTextField);
        Helper.clearBoth(phoneTextField);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        //Альтернтивное решение
        //closeButton = new ImageButton(Images.getCloseOverlay());
        //addButton = new ImageButton(Images.getAddContact());

        firstNameTextField = new HintTextFieldUnderlined("", "Имя", true, true);
        lastNameTextField = new HintTextFieldUnderlined("", "Фамилия", true, true);
        phoneTextField = new HintTextFieldUnderlined("", "Телефон", true, true);
    }

    public void setContactInfo(ContactInfo info) {
        firstNameTextField.setText(info.getFirstName());
        lastNameTextField.setText(info.getLastName());
        phoneTextField.setText(info.getPhone());
    }

    public ContactInfo getContactInfo() {
        return new ContactInfo(phoneTextField.getText().trim(),
                firstNameTextField.getText().trim(),
                lastNameTextField.getText().trim());
    }

    public void addActionListenerForAdd(ActionListener actionListener) {
        addButton.addActionListener(actionListener);
    }

    public void removeActionListenerForAdd(ActionListener actionListener) {
        addButton.removeActionListener(actionListener);
    }

    public void addActionListenerForClose(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
    }

    public void removeActionListenerForClose(ActionListener actionListener) {
        closeButton.removeActionListener(actionListener);
    }
}
