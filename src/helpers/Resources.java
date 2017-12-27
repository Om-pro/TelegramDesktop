package helpers;

import java.awt.image.BufferedImage;

/**
 * Created by HerrSergio on 19.02.2017.
 */
class Resources {

    private static BufferedImage blueButton;

    private Resources() {
    }

    public static BufferedImage getBlueButton() {
        if(blueButton == null)
            blueButton = loadImage("/img/button-background.png");
        return blueButton;
    }

    private static BufferedImage loadImage(String name) {
        return GuiHelper.loadImage(name, Resources.class);
    }
}
