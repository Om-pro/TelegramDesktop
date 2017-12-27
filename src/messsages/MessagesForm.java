package messsages;

import helpers.GuiHelper;
import org.javagram.dao.Me;
import org.javagram.dao.Message;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesForm extends JPanel {
    private JPanel rootPanel;
    private JPanel scrollPanel;
    private JScrollPane scrollPane;

    private final int width = 150;
    private final int messagesCount = 100;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy");

    private TelegramProxy telegramProxy;
    private Person person;

    {
        GuiHelper.decorateScrollPane(scrollPane);
    }

    public MessagesForm(TelegramProxy telegramProxy) {
        this(telegramProxy, null);
    }

    public MessagesForm(TelegramProxy telegramProxy, Person person) {
        this.telegramProxy = telegramProxy;
        display(person);
    }

    public void display(Person person) {

        scrollPanel.removeAll();
        this.person = person;

        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.add(Box.createGlue());

        if(person == null)
            return;

        List<Message> messages = telegramProxy.getMessages(person, messagesCount);

        for(int i = messages.size() - 1; i >= 0 ; i--) {
            JPanel panel = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    //Исправляем погань, на которую способен только BoxLayout
                    //Разрешаем растягиваться только по горизонтали
                    Dimension maxSize = super.getMaximumSize();
                    Dimension prefSize = super.getPreferredSize();
                    return new Dimension(maxSize.width, prefSize.height);
                }
            };
            Message message = messages.get(i);
            int alignment;
            Color color;
            String fontColor;
            if(message.getReceiver() instanceof Me) {
                alignment = FlowLayout.LEFT;
                color = Color.blue;
                fontColor = "white";
            } else if(message.getSender() instanceof Me) {
                alignment = FlowLayout.RIGHT;
                color = Color.cyan;
                fontColor = "black";
            } else {
                alignment = FlowLayout.CENTER;
                color = Color.red;
                fontColor = "green";
            }
            panel.setLayout(new FlowLayout(alignment));
            panel.add(new MessageForm(message.getText(), dateFormat.format(message.getDate()), width, color, fontColor));
            scrollPanel.add(panel);
        }

        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum() + scrollPane.getVerticalScrollBar().getModel().getExtent());
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
    }

    public TelegramProxy getTelegramProxy() {
        return telegramProxy;
    }

    public Person getPerson() {
        return person;
    }
}
