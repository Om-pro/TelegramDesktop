package main;

import helpers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MainForm extends JPanel {
    private JPanel rootPanel;
    private JPanel titlePanel;
    private JButton settingsButton;
    private JPanel contactsPanel;
    private JPanel messagesPanel;
    private JTextArea textArea;
    private JButton sendButton;
    private JPanel searchIconLabel;
    private JPanel searchIconPanel;
    private JTextField searchTextField;
    private JButton editButton;
    private JPanel buddyPanel;
    private JScrollPane scrollMessagesPane;

    private String meText;
    private BufferedImage mePhoto;

    private String buddyText;
    private BufferedImage buddyPhoto;

    public MainForm() {
        contactsPanel.add(new JPanel());
        messagesPanel.add(new JPanel());
        GuiHelper.decorateAsImageButton(settingsButton, Images.getSettingsIcon());
        GuiHelper.decorateAsImageButton(sendButton, Images.getMessageSendButton());
        GuiHelper.decorateAsImageButton(editButton, Images.getPenIcon());
        GuiHelper.decorateScrollPane(scrollMessagesPane);
    }

    public void addSendMessageListener(ActionListener listener) {
        this.sendButton.addActionListener(listener);
    }

    public void setContactsPanel(Component contactsPanel) {
        this.contactsPanel.removeAll();
        this.contactsPanel.add(contactsPanel);
    }

    public Component getMessagesPanel() {
        return this.messagesPanel.getComponent(0);
    }

    public void setMessagesPanel(Component messagesPanel) {
        this.messagesPanel.removeAll();
        this.messagesPanel.add(messagesPanel);
    }

    public String getMessageText() {
        return this.textArea.getText();
    }

    public void setMessageText(String text) {
        this.textArea.setText(text);
    }

    public String getMeText() {
        return meText;
    }

    public void setMeText(String meText) {
        if (!Objects.equals(this.meText, meText)) {
            this.meText = meText;
            repaint();
        }
    }

    public BufferedImage getMePhoto() {
        return mePhoto;
    }

    public void setMePhoto(BufferedImage mePhoto) {
        this.mePhoto = mePhoto;
        repaint();
    }

    public String getBuddyText() {
        return buddyText;
    }

    public void setBuddyText(String buddyText) {
        if (!Objects.equals(this.buddyText, buddyText)) {
            this.buddyText = buddyText;
            repaint();
        }
    }

    public BufferedImage getBuddyPhoto() {
        return buddyPhoto;
    }

    public void setBuddyPhoto(BufferedImage buddyPhoto) {
        this.buddyPhoto = buddyPhoto;
        repaint();
    }

    public boolean isBuddyEditEnabled() {
        return editButton.isEnabled();
    }

    public void setBuddyEditEnabled(boolean enabled) {
        editButton.setEnabled(enabled);
    }

    public void addGearEventListener(ActionListener listener) {
        this.settingsButton.addActionListener(listener);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0x82B7E8));
                g.fillRect(0, 0, this.getWidth(), this.getHeight());

                int leftMostPoint = settingsButton.getX();
                int rightMostPoint = 12;

                if (meText != null) {

                    int inset = 25;
                    Font font = Fonts.getNameFont().deriveFont(Font.ITALIC, 30);
                    Color color = Color.white;
                    String text = meText;

                    leftMostPoint = GuiHelper.drawText(g, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight() - 5, inset, true);
                }

                if (mePhoto != null) {
                    int inset = 2;
                    BufferedImage image = mePhoto;

                    leftMostPoint = GuiHelper.drawImage(g, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, true);
                }
            }
        };

        buddyPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                int leftMostPoint = editButton.getX();
                int rightMostPoint = 2;

                if (buddyPhoto != null) {
                    int inset = 2;
                    BufferedImage image = buddyPhoto;

                    rightMostPoint = GuiHelper.drawImage(graphics, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

                if (buddyText != null) {

                    int inset = 10;
                    Font font = Fonts.getNameFont().deriveFont(Font.ITALIC, 18);
                    Color color = Color.cyan;
                    String text = buddyText;

                    rightMostPoint = GuiHelper.drawText(graphics, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

            }
        };


        searchTextField = new HintTextFieldUnderlined("", "Поиск", true, false);

        searchIconPanel = new ImagePanel(Images.getSearchIcon(), true, true, 2);

    }
}
