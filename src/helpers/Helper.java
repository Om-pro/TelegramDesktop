package helpers;

import org.javagram.dao.KnownPerson;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;
import overlays.ContactInfo;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Helper {
    public static void centerAlignText(JTextPane textPane) {
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        textPane.setParagraphAttributes(attribs, false);
        clearBoth(textPane);
    }

    public static void clearBoth(JComponent textPane) {
        clearBackground(textPane);
        clearBorder(textPane);
    }

    public static void clearBackground(JComponent component) {
        component.setOpaque(false);
        component.setBackground(new Color(0, 0, 0, 0));//Для Nimbus

    }


    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small) {
        BufferedImage image;

        try {
            image = telegramProxy.getPhoto(person, small);
        } catch (Exception e) {
            e.printStackTrace();
            image = null;
        }

        if(image == null)
            image = Images.getUserImage(small);
        return image;
    }

    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small, boolean circle) {
        BufferedImage photo = getPhoto(telegramProxy, person, small);
        if(circle)
            photo = GuiHelper.makeCircle(photo);
        return photo;
    }

    public static void clearBorder(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder());
    }

    public static void decorateAsImageButton(JButton button, Image image, Image disabledImage, Color foreground) {
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(null);
        button.setBorderPainted(false);
        if(foreground != null)
            button.setBackground(foreground);
        else
            button.setText("");
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        Dimension size = button.getPreferredSize();
        button.setIcon(new ImageIcon(scaleImage(image, size.width, size.height)));
        button.setDisabledIcon(disabledImage == null ? null: new ImageIcon(scaleImage(image, size.width, size.height)));
    }

    public static BufferedImage scaleImage(Image image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.drawImage(image, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }

    public static ContactInfo toContactInfo(KnownPerson person, TelegramProxy proxy, boolean small, boolean makeCircle) {
        ContactInfo info = toContactInfo(person);
        if(proxy != null)
            info.setPhoto(getPhoto(proxy, person, small, makeCircle));
        return info;
    }

    public static ContactInfo toContactInfo(KnownPerson person) {
        return new ContactInfo(person.getPhoneNumber(), person.getFirstName(), person.getLastName(), person.getId());
    }
}
