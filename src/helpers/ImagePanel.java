package helpers;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;
    private boolean keepRatio;
    private Insets insets;

    public ImagePanel(Image image, boolean opaque, boolean keepRatio, Insets insets) {
        this.image = image;
        setOpaque(opaque);
        this.keepRatio = keepRatio;
        this.insets = insets;
    }

    public ImagePanel(Image image, boolean opaque, boolean keepRatio, int inset) {
        this(image, opaque, keepRatio, new Insets(inset, inset, inset, inset));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = this.getWidth() - (insets.left + insets.right);
        int height = this.getHeight() - (insets.top + insets.bottom);

        if (image == null || width <= 0 || height <= 0)
            return;

        if(keepRatio)
            GuiHelper.drawImage(g, image, insets.left, insets.top, width, height);
        else
            g.drawImage(image, insets.left, insets.top, width, height, null);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if(image != this.image) {
            this.image = image;
            repaint();
        }
    }

    public boolean isKeepRatio() {
        return keepRatio;
    }

    public void setKeepRatio(boolean keepRatio) {
        if(keepRatio !=this.keepRatio) {
            this.keepRatio = keepRatio;
            repaint();
        }
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        if(insets != this.insets) {
            this.insets = insets;
            repaint();
        }
    }
}