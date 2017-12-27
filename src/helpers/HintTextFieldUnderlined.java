package helpers;

import java.awt.*;

/**
 * Created by HerrSergio on 03.07.2016.
 */
public class HintTextFieldUnderlined extends HintTextField {

    private boolean underlined;
    private Color underlineColor;

    public HintTextFieldUnderlined(String text, String hint, boolean hideOnFocus, boolean underlined) {
        super(text, hint, hideOnFocus);
        this.underlined = underlined;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(boolean underlined) {
        if(this.underlined != underlined) {
            this.underlined = underlined;
            repaint();
        }
    }

    public Color getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(Color underlineColor) {
        if(this.underlineColor != underlineColor) {
            this.underlineColor = underlineColor;
            repaint();
        }
    }

    @Override
    protected void paintBorder(Graphics graphics) {
        //super.paintBorder(graphics);
        if(underlined) {
            int lineHalfWidth = 2;
            int y = this.getHeight() - lineHalfWidth * 2;
            Graphics2D g2d = (Graphics2D) graphics.create();
            try {
                g2d.setStroke(new BasicStroke(lineHalfWidth));
                Color uColor = getUnderlineColor();
                if(uColor != null)
                    g2d.setColor(uColor);
                else
                    g2d.setColor(getForeground());
                g2d.drawLine(0, y, this.getWidth(), y);
            } finally {
                g2d.dispose();
            }
        }
    }
}
