package helpers;

import java.awt.*;

/**
 * Created by HerrSergio on 20.07.2016.
 */
public class PhotoPanel extends ImagePanel {

    private boolean online;

    public PhotoPanel(Image image, boolean opaque, boolean keepRatio, Insets insets, boolean online) {
        super(image, opaque, keepRatio, insets);
        this.online = online;
    }

    public PhotoPanel(Image image, boolean opaque, boolean keepRatio, int inset, boolean online) {
        super(image, opaque, keepRatio, inset);
        this.online = online;
    }

    private final double onlineSignSize = 0.3;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(online) {

            int dx = (int)(this.getWidth() * onlineSignSize);
            int dy = (int)(this.getHeight() * onlineSignSize);

            int x = this.getWidth() - dx;
            int y = this.getHeight() - dy;

            dx -= 2;
            dy -= 2;

            g.setColor(new Color(0x00B000));
            g.fillRoundRect(x, y, dx, dy, dx, dy);

            g.setColor(new Color(0x0000B0));
            g.drawRoundRect(x, y, dx, dy, dx, dy);
        }
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        if(this.online != online) {
            this.online = online;
            repaint();
        }
    }
}
