package decoration;

import helpers.Helper;
import helpers.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ompro on 01.06.2017.
 */
public class Decoration extends JPanel {

    private JPanel rootPanel;
    private JPanel titlePanel;
    private JPanel contentPanel;
    private JButton minimizeButton;
    private JButton closeButton;

    private decoration.ComponentMover componentMover;
    private decoration.ComponentResizer componentResizer;

    public Decoration(JFrame frame) {
        setContentPanel(frame.getContentPane());
        frame.setContentPane(this);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        minimizeButton.addActionListener(e -> frame.setExtendedState(Frame.ICONIFIED));
        closeButton.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        Helper.decorateAsImageButton(closeButton, Images.getCloseIcon(), null, null);
        Helper.decorateAsImageButton(minimizeButton, Images.getMinimizeButtonImage(), null, null);

        componentMover = new ComponentMover(frame, titlePanel);
        componentResizer = new ComponentResizer(frame);
    }

    public Decoration(JDialog dialog) {
        setContentPanel(dialog.getContentPane());
        dialog.setContentPane(this);
        dialog.setUndecorated(true);
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        titlePanel.remove(minimizeButton);
        titlePanel.setBackground(new Color(0, 180, 0));

        closeButton.addActionListener(e -> dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING)));
        Helper.decorateAsImageButton(closeButton, Images.getCloseIcon(), null, null);
    }

    public static int showDialog(Frame frame, String message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Dialog dialog, String message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Frame frame, String message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Dialog dialog, String message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Frame frame, String message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, null, null, null);
    }

    public static int showDialog(Dialog dialog, String message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, null, null, null);
    }

    public static int showDialog(Window window,
                                       String message,
                                       int optionType,
                                       int messageType,
                                       Icon icon,
                                       Object[] options,
                                       Object initialValue) {
        JOptionPane optionPane = new JOptionPane(message, optionType, messageType, icon, options, initialValue);
        JDialog dialog;
        if (window instanceof Frame)
            dialog = new JDialog((Frame) window);
        else if (window instanceof Dialog)
            dialog = new JDialog((Dialog) window);
        else
            dialog = new JDialog(window);

        dialog.setModal(true);
        dialog.setContentPane(optionPane);
        new Decoration(dialog);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        Map<ActionListener, AbstractButton> listeners = new HashMap<>();
        if(options != null) {
            for (Object option : options) {
                if(option instanceof AbstractButton) {
                    AbstractButton abstractButton = (AbstractButton)option;
                    ActionListener actionListener = actionEvent -> optionPane.setValue(option);
                    abstractButton.addActionListener(actionListener);
                    listeners.put(actionListener, abstractButton);
                }
            }
        }
        PropertyChangeListener propertyChangeListener = propertyChangeEvent -> dialog.setVisible(false);
        optionPane.addPropertyChangeListener("value", propertyChangeListener);
        dialog.setVisible(true);
        optionPane.removePropertyChangeListener("value", propertyChangeListener);
        for(Map.Entry<ActionListener, AbstractButton> entry : listeners.entrySet())
            entry.getValue().removeActionListener(entry.getKey());
        Object selectedValue = optionPane.getValue();
        if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;

        //If there is not an array of option buttons:
        if(options == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue);
            else
                return JOptionPane.CLOSED_OPTION;
        }
        //If there is an array of option buttons:
        for(int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
            if(options[counter].equals(selectedValue))
                return counter;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    public void setContentPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public Component getContentPanel() {
        return contentPanel.getComponent(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

//        contentPanel = new ImagePanel(Images.getBackground(), false, false, 0);
    }
}