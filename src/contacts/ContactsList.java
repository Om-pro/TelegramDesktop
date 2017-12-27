package contacts;

import helpers.GuiHelper;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class ContactsList extends JPanel {
    private JPanel rootPanel;
    private JScrollPane scrollPane;
    private JList<Person> contactList;

    private TelegramProxy telegramProxy;

    {
        GuiHelper.decorateScrollPane(scrollPane);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
    }

    public TelegramProxy getTelegramProxy() {
        return telegramProxy;
    }

    public void setTelegramProxy(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;

        if(telegramProxy != null) {
            List<Person> dialogs = telegramProxy.getPersons();
            contactList.setCellRenderer(new ContactRenderer(telegramProxy));
            contactList.setListData(dialogs.toArray(new Person[dialogs.size()]));
        } else {
            contactList.setCellRenderer(new DefaultListCellRenderer());
            contactList.setListData(new Person[0]);
        }
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener)  {
        contactList.addListSelectionListener(listSelectionListener);
    }

    public void removeListSelectionListener(ListSelectionListener listSelectionListener)  {
        contactList.removeListSelectionListener(listSelectionListener);
    }

    public Person getSelectedValue() {
        return contactList.getSelectedValue();
    }

    public void setSelectedValue(Person person) {
        if(person != null) {
            ListModel<Person> model = contactList.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                if (model.getElementAt(i).equals(person)) {
                    contactList.setSelectedIndex(i);
                    return;
                }
            }
        }
        contactList.clearSelection();
    }
}
