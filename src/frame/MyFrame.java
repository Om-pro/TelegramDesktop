package frame;

import contacts.ContactsList;
import decoration.Decoration;
import helpers.BlueButton;
import helpers.Helper;
import helpers.MyLayeredPane;
import helpers.OverlayDialog;
import intro.CheckCodeForm;
import intro.MobileNumberForm;
import intro.RegForm;
import main.MainForm;
import messsages.MessagesForm;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;
import org.javagram.dao.proxy.changes.UpdateChanges;
import overlays.ContactInfo;
import overlays.PlusOverlay;
import overlays.ProfileForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Ompro on 01.06.2017.
 */
public class MyFrame extends JFrame {

    private MobileNumberForm mobileNumberForm = new MobileNumberForm();
    private RegForm regForm = new RegForm();
    private CheckCodeForm checkCodeForm = new CheckCodeForm();
    private ContactsList contactsList = new ContactsList();

    private MainForm mainForm = new MainForm();
    private ProfileForm profileForm = new ProfileForm();
    private PlusOverlay plusOverlay = new PlusOverlay();
    private MyLayeredPane contactsLayeredPane = new MyLayeredPane();

    private Decoration decoration = new Decoration(this);

    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;

    OverlayDialog overlayDialog = new OverlayDialog();

    private Timer timer;
    private int messagesFrozen;

    public MyFrame(TelegramDAO telegramDAO) {

        this.telegramDAO = telegramDAO;

        setContentPanel(mobileNumberForm);

        showPhoneNumberRequest(false);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (showQuestionMessage("Вы уверены, что хотите выйти?", "Подтверждение выхода"))
                    exit();
            }
        });

        setSize(800, 600);
        setMinimumSize(new Dimension(400, 500));

        mobileNumberForm.addActionListenerForSwitchAction((ActionEvent e) -> {
            String number = mobileNumberForm.getFormattedValue();
            if (number == null) {
                showPhoneNumberEmptyDialog();
            } else {
                try {
                    try {
                        telegramDAO.acceptNumber(number.replaceAll("[\\D]+", ""));

                        if (telegramDAO.canSignIn()) {
                            telegramDAO.sendCode();
                            setContentPanel(checkCodeForm);
                        } else {
                            setContentPanel(regForm);
                        }
                        checkCodeForm.setPhoneLabelText(number);
                    } catch (ApiException e1) {
                        if (e1.isPhoneNumberInvalid()) {
                            showPhoneNumberInvalid();
                            return;
                        }
                        throw e1;
                    }
                } catch (Exception e1) {
                    catchException(e1);
                }
            }
        });

        regForm.addActionListenerForSwitchAction(e -> {

            String firstName = regForm.getNameField();
            String lastName = regForm.getSurnameField();

            if (firstName.isEmpty() && lastName.isEmpty()) {
                Decoration.showDialog(MyFrame.this, "Введите имя и/или фамилию", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
            } else {
                try {
                    telegramDAO.sendCode();
                    setContentPanel(checkCodeForm);
                } catch (Exception e1) {
                    catchException(e1);
                }
            }
        });

        checkCodeForm.addActionListenerForSwitchAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstName = regForm.getNameField();
                String lastName = regForm.getSurnameField();
                char[] code = checkCodeForm.getPasswordField();

                if (code.length == 0) {
                    showCodeEmptyDialog();
                } else {
                    try {
                        String codeToString = String.valueOf(code);
                        Arrays.fill(code, '\0');
                        try {

                            if (telegramDAO.canSignIn()) {
                                telegramDAO.signIn(codeToString);

                            } else {
                                telegramDAO.signUp(codeToString, firstName, lastName);
                            }
                            setContentPanel(overlayDialog);
                            mainForm.setContactsPanel(contactsList);
                            overlayDialog.setContentPanel(mainForm);
                            createTelegramProxy();
                            telegramProxy.getMe();
                        } catch (ApiException e1) {
                            if (e1.isCodeInvalid()) {
                                showCodeInvalid();
                                return;
                            }
                            if (e1.isCodeEmpty()) {
                                showCodeEmpty();
                                return;
                            }
                            if (e1.isCodeExpired()) {
                                showCodeExpired();
                                return;
                            }
                            if (e1.isNameInvalid()) {
                                showNameInvalid();
                                return;
                            }
                            throw e1;
                        }
                    } catch (Exception e1) {
                        catchException(e1);
                    }
                }
            }
        });

        mainForm.setContactsPanel(contactsLayeredPane);
        contactsLayeredPane.add(contactsList, new Integer(0));
        contactsLayeredPane.add(plusOverlay, new Integer(1));

        mainForm.addGearEventListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Me me = telegramProxy.getMe();
                ContactInfo contactInfo = Helper.toContactInfo(me, telegramProxy, false, false);
                profileForm.setContactInfo(contactInfo);
                overlayDialog.setOverlayPanel(profileForm);
            }
        });

        contactsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {

                if (listSelectionEvent.getValueIsAdjusting() || messagesFrozen != 0)
                    return;

                if (telegramProxy == null) {
                    displayDialog(null);
                } else {
                    displayDialog(contactsList.getSelectedValue());
                }
            }
        });

        mainForm.addSendMessageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Person buddy = contactsList.getSelectedValue();
                String text = mainForm.getMessageText().trim();
                if (telegramProxy != null && buddy != null && !text.isEmpty()) {
                    try {
                        telegramProxy.sendMessage(buddy, text);
                        mainForm.setMessageText("");
                        checkForUpdates(true);
                    } catch (Exception e) {
                        showWarningMessage("Не могу отправить сообщение", "Ошибка!");
                    }
                }
            }
        });

        profileForm.addActionListenerForClose(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                overlayDialog.setOverlayPanel(null);
            }
        });

        profileForm.addActionListenerForLogout(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchToBegin();
            }
        });

        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checkForUpdates(false);
            }
        });
        timer.start();
    }


    protected void checkForUpdates(boolean force) {
        if (telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update(force ? TelegramProxy.FORCE_SYNC_UPDATE : TelegramProxy.USE_SYNC_UPDATE);

            int photosChangedCount = updateChanges.getLargePhotosChanged().size() +
                    updateChanges.getSmallPhotosChanged().size() +
                    updateChanges.getStatusesChanged().size();

            if (updateChanges.getListChanged()) {
                updateContacts();
            } else if (photosChangedCount != 0) {
                contactsList.repaint();
            }

            Person currentBuddy = getMessagesForm().getPerson();
            Person targetPerson = contactsList.getSelectedValue();

            Dialog currentDialog = currentBuddy != null ? telegramProxy.getDialog(currentBuddy) : null;

            if (!Objects.equals(targetPerson, currentBuddy) ||
                    updateChanges.getDialogsToReset().contains(currentDialog) ||
                    //updateChanges.getDialogsChanged().getChanged().containsKey(currentDialog) ||
                    updateChanges.getDialogsChanged().getDeleted().contains(currentDialog)) {
                updateMessages();
            } else if (updateChanges.getPersonsChanged().getChanged().containsKey(currentBuddy)
                    || updateChanges.getSmallPhotosChanged().contains(currentBuddy)
                    || updateChanges.getLargePhotosChanged().contains(currentBuddy)) {
                displayBuddy(targetPerson);
            }

            if (updateChanges.getPersonsChanged().getChanged().containsKey(telegramProxy.getMe())
                    || updateChanges.getSmallPhotosChanged().contains(telegramProxy.getMe())
                    || updateChanges.getLargePhotosChanged().contains(telegramProxy.getMe())) {
                displayMe(telegramProxy.getMe());
            }
        }
    }

    private JButton[] okButton = BlueButton.createDecoratedButtons(JOptionPane.DEFAULT_OPTION);
    private JButton[] yesNoButtons = BlueButton.createDecoratedButtons(JOptionPane.YES_NO_OPTION);

    private void showPhoneNumberEmptyDialog() {
        Decoration.showDialog(MyFrame.this, "Проверьте правильность введённого номера", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
    }

    private void showErrorMessage(String text, String title) {
        Decoration.showDialog(this, text, title, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                okButton, okButton[0]);
    }

    private void showCodeEmptyDialog() {
        Decoration.showDialog(MyFrame.this, "Введён неверный код!", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
    }

    private boolean showQuestionMessage(String text, String title) {
        return Decoration.showDialog(this, text, title, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                yesNoButtons, yesNoButtons[0]) == 0;
    }

    private void showWarningMessage(String text, String title) {
        Decoration.showDialog(this, text, title, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                okButton, okButton[0]);
    }

    private void showPhoneNumberInvalid() {
        showWarningMessage("Номер телефона введен не верно", "Внимание!");
        showPhoneNumberRequest(true);
    }

    private void showNameInvalid() {
        showWarningMessage("Неверные регистрационные данные", "Внимание!");
        showNameRequest(false);
    }

    private void setContentPanel(Container container) {
        decoration.setContentPanel(container);
    }

    private void sendCode() throws IOException, ApiException {
        telegramDAO.sendCode();
    }

    private void showCodeInvalid() {
        showWarningMessage("Неверный код", "Внимание!");
    }

    private void showCodeEmpty() {
        showWarningMessage("Не введен код", "Внимание!");
    }

    private void showCodeExpired() throws IOException, ApiException {
        showWarningMessage("Код устарел. Отправляю новый", "Внимание!");
        sendAndRequestCode();
    }

    private void sendAndRequestCode() throws IOException, ApiException {
        sendCode();
    }

    private void catchException(Exception e) {
        if (e instanceof IOException) {
            showErrorMessage("Потеряно соединение с сервером", "Ошибка!");
        } else if (e instanceof ApiException) {
            showErrorMessage("Непредвиденная ошибка API :: " + e.getMessage(), "Ошибка!");
        } else {
            showErrorMessage("Непредвиденная ошибка", "Ошибка!");
        }
        abort(e);
    }

    private void showPhoneNumberRequest(boolean clear) {
        setContentPanel(mobileNumberForm);
        if (clear)
            mobileNumberForm.clear();
        mobileNumberForm.transferFocusTo();
    }

    private void showNameRequest(boolean clear) {
        setContentPanel(regForm);
        if (clear)
            regForm.clear();
    }

    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            displayBuddy(person);
            revalidate();
            repaint();
        } catch (Exception e) {
            showErrorMessage("Проблема соединения с сервером", "проблемы в сети");
            abort(e);
        }
    }

    private void displayMe(Me me) {
        if (me == null) {
            mainForm.setMeText(null);
            mainForm.setMePhoto(null);
        } else {
            mainForm.setMeText(me.getFirstName() + " " + me.getLastName());
            mainForm.setMePhoto(Helper.getPhoto(telegramProxy, me, true, true));
        }
    }

    private void displayBuddy(Person person) {
        if (person == null) {
            mainForm.setBuddyText(null);
            mainForm.setBuddyPhoto(null);
            mainForm.setBuddyEditEnabled(false);
        } else {
            mainForm.setBuddyText(person.getFirstName() + " " + person.getLastName());
            mainForm.setBuddyPhoto(Helper.getPhoto(telegramProxy, person, true, true));
            mainForm.setBuddyEditEnabled(person instanceof Contact);
        }
    }


    private void createTelegramProxy() throws ApiException {
        telegramProxy = new TelegramProxy(telegramDAO);
        updateTelegramProxy();
    }

    private void destroyTelegramProxy() {
        telegramProxy = null;
        updateTelegramProxy();
    }

    private void updateTelegramProxy() {
        messagesFrozen++;
        try {
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(null);
            createMessagesForm();
            displayDialog(null);
            displayMe(telegramProxy != null ? telegramProxy.getMe() : null);
        } finally {
            messagesFrozen--;
        }

        mainForm.revalidate();
        mainForm.repaint();
    }

    private void updateContacts() {
        messagesFrozen++;
        try {
            Person person = contactsList.getSelectedValue();
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(person);
        } finally {
            messagesFrozen--;
        }
    }

    private void updateMessages() {
        displayDialog(contactsList.getSelectedValue());
        mainForm.revalidate();
        mainForm.repaint();
    }

    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        mainForm.setMessagesPanel(messagesForm);
        mainForm.revalidate();
        mainForm.repaint();
        return messagesForm;
    }

    private MessagesForm getMessagesForm() {
        if (mainForm.getMessagesPanel() instanceof MessagesForm) {
            return (MessagesForm) mainForm.getMessagesPanel();
        } else {
            return createMessagesForm();
        }
    }

    private void abort(Throwable e) {
        if (e != null)
            e.printStackTrace();
        else
            System.err.println("Unknown Error");
        try {
            telegramDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(-1);
    }

    private void switchToBegin() {
        try {
            destroyTelegramProxy();
            overlayDialog.setOverlayPanel(null);
            showPhoneNumberRequest(true);
            telegramDAO.logOut();
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void exit() {
        try {
            telegramDAO.close();
            System.exit(0);
        } catch (Exception e) {
            abort(e);
        }
    }

}
