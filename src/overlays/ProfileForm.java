package overlays;

import helpers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ProfileForm extends OverlayBackground {
    private JPanel rootPanel;
    private JButton logoutButton;
    private JButton closeButton;
    private JLabel phoneLabel;
    private JLabel nameLabel;
    private JPanel photoPanel;

    private final static String phoneRegexFrom = "^\\+?(\\d*)(\\d{3})(\\d{3})(\\d{2})(\\d{2})$", phoneRegexTo = "+$1($2)$3-$4-$5";

    private String phone;
    private int id = 0;


    {
        nameLabel.setFont(Fonts.getNameFont().deriveFont(0, 45));
        nameLabel.setForeground(Color.white);

        phoneLabel.setFont(Fonts.getNameFont().deriveFont(0, 30));
        phoneLabel.setForeground(Color.white);

        GuiHelper.decorateAsImageButton(closeButton, Images.getCloseOverlay());
        GuiHelper.decorateAsImageButton(logoutButton, Images.getLogoutIcon());
    }

    public void addActionListenerForLogout(ActionListener actionListener) {
        logoutButton.addActionListener(actionListener);
    }

    public void addActionListenerForClose(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
    }

    public ContactInfo getContactInfo() {
        ContactInfo info = new ContactInfo();
        String[] data = nameLabel.getText().trim().split("\\s+", 2); //На случай редактирования, которого пока нет
        info.setFirstName(data.length > 0 ? data[0] : "");
        info.setLastName(data.length > 1 ? data[1] : "");
        info.setPhone(phone);
        info.setPhoto((BufferedImage) ((ImagePanel) photoPanel).getImage());
        info.setId(id);
        return info;
    }

    public void setContactInfo(ContactInfo contactInfo) {

        if (contactInfo != null) {
            ((ImagePanel) photoPanel).setImage(contactInfo.getPhoto());
            nameLabel.setText(contactInfo.getFirstName() + " " + contactInfo.getLastName());
            phone = contactInfo.getPhone();
            phoneLabel.setText(contactInfo.getClearedPhone().replaceAll(phoneRegexFrom, phoneRegexTo));
            id = contactInfo.getId();
        } else {
            ((ImagePanel) photoPanel).setImage(null);
            nameLabel.setText("");
            phone = "";
            phoneLabel.setText("");
            id = 0;
        }

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        photoPanel = new ImagePanel(null, true, true, 0);
    }
}