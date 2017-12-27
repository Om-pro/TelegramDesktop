package helpers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 * Created by HerrSergio on 14.06.2016.
 */
public class GuiHelper {

    private GuiHelper() {

    }

    public static Rectangle drawImage(Graphics g, Image image, int x, int y, int width, int height) {
        Rectangle rect = getAreaFor(new Rectangle(x, y, width, height), new Dimension(image.getWidth(null), image.getHeight(null)));
        g.drawImage(image, rect.x, rect.y, rect.width, rect.height, null);
        return rect;
    }

    public static int drawImage(Graphics g, BufferedImage image, int x, int y, int width, int height, int inset, boolean right) {
        int photoHeight = height - inset * 2;
        int photoWidth = width - inset * 2;

        x += inset;
        y += inset;

        Rectangle area = getAreaFor(new Rectangle(x, y, photoWidth, photoHeight), new Dimension(image.getWidth(), image.getHeight()));

        if(right) {
            x += width - area.width;
        }

        g.drawImage(image, x, area.y, area.width, area.height, null);

        if(right)
            return x - inset;
        else
            return x + area.width + inset;

    }

    public static Rectangle getAreaFor(Rectangle area, Dimension size) {
        int x = area.x;
        int y = area.y;
        double dx = area.width / size.getWidth();
        double dy = area.height / size.getHeight();
        double d = Math.min(dy, dx);
        int newWidth = (int)Math.round(d * size.getWidth());
        int newHeight = (int)Math.round(d * size.getHeight());
        x += (area.width - newWidth) / 2;
        y += (area.height - newHeight) / 2;
        return new Rectangle(x, y, newWidth, newHeight);
    }

    public static int drawText(Graphics g, String text, Color color, Font font, int x, int y, int width, int height, int inset, boolean right) {

        String line = text;

        x += inset;
        int maxWidth = width - inset * 2;
        FontMetrics fontMetrics = g.getFontMetrics(font);

        while (fontMetrics.stringWidth(line) > maxWidth) {
            if (line.length() > 3)
                line = line.substring(0, line.length() - 4) + "...";
            else if(right)
                return x + maxWidth - inset;
            else
                return x + inset;
        }

        LineMetrics lineMetrics = fontMetrics.getLineMetrics(line, g);
        y += (int) Math.round((height - lineMetrics.getHeight()) / 2.0 + fontMetrics.getAscent());

        if(right)
            x += maxWidth - fontMetrics.stringWidth(line);

        g.setColor(color);
        g.setFont(font);
        g.drawString(line, x, y);

        if(right)
            return x - inset;
        else
            return x + fontMetrics.stringWidth(line) + inset;
    }

    public static Color makeTransparent(Color color, float transparency) {
        if(transparency < 0.0f || transparency > 1.0f)
            throw new IllegalArgumentException();
        return new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)Math.round(color.getAlpha() * transparency));
    }

    private static ColorConvertOp grayOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public static BufferedImage makeGray(Image image){
        BufferedImage bufferedImage = null;
        if(image instanceof BufferedImage)
            bufferedImage = (BufferedImage) image;
        else
            bufferedImage = copyImage(image);
        return grayOp.filter(bufferedImage, null);
    }

    public static BufferedImage makeCircle(Image image) {
        BufferedImage circle = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = circle.createGraphics();
        try {
            g2d.clip(new Ellipse2D.Double(0, 0, circle.getWidth(), circle.getHeight()));
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return circle;
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

    public static BufferedImage copyImage(Image image) {
        return scaleImage(image, image.getWidth(null), image.getHeight(null));
    }

//    public static void decorateScrollPane(JScrollPane scrollPane) {
//        int width = 3;
//
//        JScrollBar verticalScrollBar =  scrollPane.getVerticalScrollBar();
//        verticalScrollBar.setUI(new MyScrollbarUI());
//        verticalScrollBar.setPreferredSize(new Dimension(width, Integer.MAX_VALUE));
//
//        JScrollBar horizontalScrollBar =  scrollPane.getHorizontalScrollBar();
//        horizontalScrollBar.setUI(new MyScrollbarUI());
//        horizontalScrollBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, width));
//
//        for (String corner : new String[] {ScrollPaneConstants.LOWER_RIGHT_CORNER, ScrollPaneConstants.LOWER_LEFT_CORNER,
//                ScrollPaneConstants.UPPER_LEFT_CORNER, ScrollPaneConstants.UPPER_RIGHT_CORNER}) {
//            JPanel panel = new JPanel();
//            panel.setBackground(Color.white);
//            scrollPane.setCorner(corner, panel);
//        }
//    }

    public static void decorateAsImageButton(JButton button, Dimension size, Image image) {
        decorateAsImageButton(null, button, size, image, null);
    }

    public static void decorateAsImageButton(Color foreground, JButton button, Dimension size, Image image) {
        decorateAsImageButton(foreground, button, size, image, null);
    }

    public static void decorateAsImageButton(JButton button, Image image, Image disabledImage) {
        decorateAsImageButton(null, button, button.getPreferredSize(), image, disabledImage);
    }

    public static void decorateAsImageButton(Color foreground, JButton button, Image image, Image disabledImage) {
        decorateAsImageButton(foreground, button, button.getPreferredSize(), image, disabledImage);
    }

    public static void decorateAsImageButton(JButton button, Image image) {
        decorateAsImageButton(null, button, button.getPreferredSize(), image, null);
    }

    public static void decorateAsImageButton(Color foreground, JButton button, Image image) {
        decorateAsImageButton(foreground, button, button.getPreferredSize(), image, null);
    }

    public static void decorateAsImageButton(JButton button, Dimension size, Image image, Image disabledImage) {
        decorateAsImageButton(null, button, size, image, disabledImage);
    }

    public static void decorateAsImageButton(Color foreground, JButton button, Dimension size, Image image, Image disabledImage) {
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        if(foreground == null)
            button.setText("");
        else
            button.setForeground(foreground);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);

        button.setIcon(getImageIconFor(image, size));
        if(image == null) {
            disabledImage = null;
        } else if(disabledImage == null) {
            if (foreground == null)
                disabledImage = createTransparentImage(1, 1);
        }
        button.setDisabledIcon(getImageIconFor(disabledImage, size));

        button.setSelectedIcon(null);
        button.setDisabledSelectedIcon(null);
        button.setPressedIcon(null);
        button.setRolloverIcon(null);
        button.setRolloverSelectedIcon(null);
    }

    public static ImageIcon getImageIconFor(Image image, Dimension size) {
        if(image == null)
            return null;
        if(size != null)
            image = scaleImage(image, size.width, size.height);
        else
            image = copyImage(image);
        return new ImageIcon(image);
    }

    public static BufferedImage createTransparentImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.drawImage(image, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }

    public static BufferedImage loadImage(String name, Class root) {
        try {
            return ImageIO.read(Images.class.getResource(name));
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    public static void decorateScrollPane(JScrollPane scrollPane) {
        int width = 3;

        JScrollBar verticalScrollBar =  scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new MyScrollbarUI());
        verticalScrollBar.setPreferredSize(new Dimension(width, Integer.MAX_VALUE));

        JScrollBar horizontalScrollBar =  scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new MyScrollbarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, width));

        for (String corner : new String[] {ScrollPaneConstants.LOWER_RIGHT_CORNER, ScrollPaneConstants.LOWER_LEFT_CORNER,
                ScrollPaneConstants.UPPER_LEFT_CORNER, ScrollPaneConstants.UPPER_RIGHT_CORNER}) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.white);
            scrollPane.setCorner(corner, panel);
        }
    }
}
