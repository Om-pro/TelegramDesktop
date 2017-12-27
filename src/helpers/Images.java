package helpers;

import java.awt.image.BufferedImage;

public class Images {
    private Images() {
    }

    private static BufferedImage background;
    private static BufferedImage logo;
    private static BufferedImage logoMini;
    private static BufferedImage minimizeButtonImage;
    private static BufferedImage sendButtonImage;
    private static BufferedImage closeIcon;
    private static BufferedImage phoneIcon;
    private static BufferedImage lockIcon;
    private static BufferedImage settingsIcon;
    private static BufferedImage sendButton;
    private static BufferedImage smallUserImage;
    private static BufferedImage largeUserImage;
    private static BufferedImage searchIcon;
    private static BufferedImage penIcon;
    private static BufferedImage penLogo;
    private static BufferedImage closeOverlay;
    private static BufferedImage logoutIcon;

    public synchronized static BufferedImage getBackground() {
        if (background == null)
                background = getImage("background.png");

        return background;
    }

    public synchronized static BufferedImage getCloseIcon() {
        if(closeIcon == null)
            closeIcon = getImage("close-icon.png");
        return closeIcon;
    }

    public synchronized static BufferedImage getMinimizeButtonImage() {
        if (minimizeButtonImage == null)
            minimizeButtonImage = getImage("minimize.png");

        return minimizeButtonImage;
    }

    public synchronized static BufferedImage getLogo() {
        if (logo == null)
            logo = getImage("logo.png");
        return logo;
    }

    public synchronized static BufferedImage getLogoMini() {
        if (logoMini == null)
            logoMini = getImage("logo-mini.png");
        return logoMini;
    }

    public synchronized static BufferedImage getSendButton() {
        if (sendButtonImage == null)
            sendButtonImage = getImage("button-background.png");
        return sendButtonImage;
    }

    public synchronized static BufferedImage getLockIcon() {
        if(lockIcon == null)
            lockIcon = getImage("icon-lock.png");
        return lockIcon;
    }

    public synchronized static BufferedImage getPhoneIcon() {
        if(phoneIcon == null)
            phoneIcon = getImage("icon-phone.png");
        return phoneIcon;
    }

    public synchronized static BufferedImage getSettingsIcon() {
        if(settingsIcon == null)
            settingsIcon = getImage("icon-settings.png");
        return settingsIcon;
    }

    public synchronized static BufferedImage getMessageSendButton() {
        if(sendButton == null)
            sendButton = getImage("button-send.png");
        return sendButton;
    }

    public synchronized static BufferedImage getSmallUserImage() {
        if (smallUserImage == null)
            smallUserImage = getImage("images (2).jpg");
        return smallUserImage;
    }

    public synchronized static BufferedImage getLargeUserImage() {
        if (largeUserImage == null)
            largeUserImage = getImage("User-icon.png");
        return largeUserImage;
    }

    public synchronized static BufferedImage getSearchIcon() {
        if (searchIcon == null)
            searchIcon = getImage("icon-search.png");
        return searchIcon;
    }

    public static BufferedImage getPenIcon() {
        if(penIcon == null)
            penIcon = getImage("icon-edit.png");
        return penIcon;
    }

    public static BufferedImage getPenLogo() {
        if(penLogo == null)
            penLogo = getImage("icon-edit.png");
        return penLogo;
    }

    public static BufferedImage getCloseOverlay() {
        if(closeOverlay == null)
            closeOverlay = getImage("Close.png");
        return closeOverlay;
    }

    public static BufferedImage getLogoutIcon() {
        if(logoutIcon == null)
            logoutIcon = getImage("logout-icon.png");
        return logoutIcon;
    }

    private static BufferedImage getImage(String name) {
        return GuiHelper.loadImage("/img/" + name, Images.class);
    }

    public static BufferedImage getUserImage(boolean small) {
        return small ? getSmallUserImage() : getLargeUserImage();
    }
}