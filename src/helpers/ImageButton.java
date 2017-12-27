package helpers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HerrSergio on 27.06.2016.
 */
public class ImageButton extends JButton {

    private Image image;
    private boolean keepRatio;
    private Image disabledImage;
    private boolean keepDisabledRatio;

    {
        setOpaque(false);
        setBorder(null);
        setBorderPainted(false);
    }

    public ImageButton(Image image) {
        this(image, null);
    }

    public ImageButton(Image image, Image disabledImage) {
        this(image, true, disabledImage, true);
    }

    public ImageButton(Image image, boolean keepRatio, Image disabledImage, boolean keepDisabledRatio) {
        this.image = image;
        this.keepRatio = keepRatio;
        this.disabledImage = image == null ? null : disabledImage;
        this.keepDisabledRatio = keepDisabledRatio;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (isOpaque()) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
        if(image != null) {
            Insets insets = getInsets();
            int width = this.getWidth() - (insets.left + insets.right);
            int height = this.getHeight() - (insets.top + insets.bottom);
            int x = insets.left;
            int y = insets.top;
            if (isEnabled()) {
                if (keepRatio)
                    GuiHelper.drawImage(graphics, image, x, y, width, height);
                else
                    graphics.drawImage(image, x, y, width, height, null);
            } else if (disabledImage != null) {
                if (keepDisabledRatio)
                    GuiHelper.drawImage(graphics, disabledImage, x, y, width, height);
                else
                    graphics.drawImage(disabledImage, x, y, width, height, null);
            }
        }

    }
}
