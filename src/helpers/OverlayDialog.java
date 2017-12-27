package helpers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HerrSergio on 21.02.2017.
 */
public class OverlayDialog extends MyBufferedOverlayDialog {

    private JPanel contentPanel;
    private JPanel overlayPanel;

    private static final int CONTENT_PANEL = -1, OVERLAY_PANEL = 0;

    public OverlayDialog() {
        super(new JPanel(), new JPanel());

        contentPanel = (JPanel) getBackgroundComponent();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());

        overlayPanel = (JPanel) getForegroundComponent(0);
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BorderLayout());
    }

    public void setContentPanel(Component component) {
        if(getContentPanel() != component) {
            contentPanel.removeAll();
            if (component != null)
                contentPanel.add(component);
            invalidateFakeBackground();
            revalidate();
            repaint();
        }
    }

    public Component getContentPanel() {
        if(contentPanel.getComponents().length > 0)
            return contentPanel.getComponent(0);
        else
            return null;
    }

    public void setOverlayPanel(Component component) {
        if(getOverlayPanel() != component) {
            overlayPanel.removeAll();
            if(component != null) {
                overlayPanel.add(component);
                setIndex(OVERLAY_PANEL);
            } else {
                setIndex(CONTENT_PANEL);
            }
            revalidate();
            repaint();
        }
    }

    public Component getOverlayPanel() {
        if(overlayPanel.getComponents().length > 0)
            return overlayPanel.getComponent(0);
        else
            return null;
    }
}
