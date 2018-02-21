package overlays;

import helpers.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class EditContactForm extends OverlayBackground {
    private JPanel contactPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField phoneTextField;
    private JPanel photoPanel;
    private JButton closeButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JPanel rootPanel;

    private int id;

    {
        setContactInfo(new ContactInfo());

        ((HintTextField)firstNameTextField).setHintAlignment(JTextField.CENTER);
        ((HintTextField)lastNameTextField).setHintAlignment(JTextField.CENTER);
        ((HintTextField)phoneTextField).setHintAlignment(JTextField.CENTER);

        GuiHelper.decorateAsImageButton(closeButton, Images.getCloseOverlay());
        GuiHelper.decorateAsImageButton(deleteButton, Images.getRemoveContact());
        GuiHelper.decorateAsImageButton(saveButton, Images.getUpdateContact());

        Helper.clearBoth(firstNameTextField);
        Helper.clearBoth(lastNameTextField);
        Helper.clearBoth(phoneTextField);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        //Альтернтивное решение
        //closeButton = new ImageButton(Images.getCloseOverlay());
        //deleteButton = new ImageButton(Images.getRemoveContact());
        //saveButton = new ImageButton(Images.getUpdateContact());

        firstNameTextField = new HintTextFieldUnderlined("", "Имя", true, true);
        lastNameTextField = new HintTextFieldUnderlined("", "Фамилия", true, true);
        phoneTextField = new HintTextFieldUnderlined("", "Телефон", true, true);

        photoPanel = new ImagePanel(null, true, false, 0);
    }

    public void setContactInfo(ContactInfo info) {
        firstNameTextField.setText(info.getFirstName());
        lastNameTextField.setText(info.getLastName());
        phoneTextField.setText(info.getPhone());
        ((ImagePanel)photoPanel).setImage(info.getPhoto());
        id = info.getId();
    }

    public ContactInfo getContactInfo() {
        ContactInfo info = new ContactInfo();
        info.setPhone(phoneTextField.getText().trim());
        info.setFirstName(firstNameTextField.getText().trim());
        info.setLastName(lastNameTextField.getText().trim());
        info.setPhoto((BufferedImage) ((ImagePanel)photoPanel).getImage());
        info.setId(id);
        return info;
    }

    public void addActionListenerForSave(ActionListener actionListener) {
        saveButton.addActionListener(actionListener);
    }

    public void removeActionListenerForSave(ActionListener actionListener) {
        saveButton.removeActionListener(actionListener);
    }

    public void addActionListenerForRemove(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    public void removeActionListenerForRemove(ActionListener actionListener) {
        deleteButton.removeActionListener(actionListener);
    }

    public void addActionListenerForClose(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
    }

    public void removeActionListenerForClose(ActionListener actionListener) {
        closeButton.removeActionListener(actionListener);
    }
}

