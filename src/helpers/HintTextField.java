package helpers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

/**
 * Created by HerrSergio on 29.04.2016.
 */
public class HintTextField extends JTextField {

    private String hint;

    private Font hintFont;
    private Color hintForeground;
    private boolean hideOnFocus = true;
    private int hintAlignment = JTextField.LEFT;

    public HintTextField(String text, String hint, boolean hideOnFocus) {
        super();

        this.hint = hint;
        this.hideOnFocus = hideOnFocus;

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                HintTextField.this.focusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                HintTextField.this.focusLost();
            }
        });

        setText(text);
    }

    public HintTextField(String text, String hint) {
        this(text, hint, true);
    }

    public HintTextField(String hint) {
        this("", hint);
    }

    public HintTextField() {
        this("...");
    }

    protected void focusGained() {
        if(hideOnFocus)
            repaint();
    }

    protected void focusLost() {
        if(hideOnFocus)
            repaint();
    }

    public void setHint(String hint) {
        if(!Objects.equals(this.hint, hint)){
            this.hint = hint;
            repaint();
        }
    }

    public String getHint() {
        return hint;
    }


    protected Font createHintFont() {
        if(getHintFont() != null)
            return getHintFont();
        else
            return getFont().deriveFont(getFont().getStyle() ^ Font.PLAIN);
    }

    protected Color createHintForeground() {
        if(getHintForeground() != null)
            return getHintForeground();
        else
            return getForeground().brighter().brighter().brighter();
    }

    protected boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public Font getHintFont() {
        return hintFont;
    }

    public void setHintFont(Font hintFont) {
        this.hintFont = hintFont;
        repaint();
    }

    public Color getHintForeground() {
        return hintForeground;
    }

    public void setHintForeground(Color hintForeground) {
        this.hintForeground = hintForeground;
        repaint();
    }

    public boolean isHideOnFocus() {
        return hideOnFocus;
    }

    public void setHideOnFocus(boolean hideOnFocus) {
        if(this.hideOnFocus != hideOnFocus) {
            this.hideOnFocus = hideOnFocus;
            repaint();
        }
    }

    public int getHintAlignment() {
        return hintAlignment;
    }

    public void setHintAlignment(int hintAlignment) {
        if(this.hintAlignment != hintAlignment) {
            this.hintAlignment = hintAlignment;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isEmpty(getText()) && !(isFocusOwner() && hideOnFocus)) {
            Insets insets = getInsets();
            Insets margins = getMargin();
            int left = insets.left + margins.left;
            int right = insets.right + margins.right;
            int maxWidth = getWidth() - (left + right);
            g.setFont(createHintFont());
            g.setColor(createHintForeground());
            FontMetrics fontMetrics = g.getFontMetrics();
            String hint = this.hint;
            while(fontMetrics.stringWidth(hint) > maxWidth) {
                int len = hint.length() - 4;
                if(len < 0) {
                    break;
                } else {
                    hint = hint.substring(0, len) + "...";
                }
            }
            int x = left;
            if(hintAlignment == JTextField.RIGHT) {
                x += maxWidth - fontMetrics.stringWidth(hint);
            } else if(hintAlignment == JTextField.CENTER) {
                x += (maxWidth - fontMetrics.stringWidth(hint)) / 2;
            }
            int y = getBaseline(getWidth(), getHeight());
            g.drawString(hint, x, y);
        }
    }
}
