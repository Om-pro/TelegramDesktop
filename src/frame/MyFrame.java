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
    private OverlayDialog overlayDialog = new OverlayDialog();

    private Decoration decoration;

    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;

    private MyLayeredPane contactsLayeredPane = new MyLayeredPane();

    private int messagesFrozen;


    public MyFrame(TelegramDAO telegramDAO) {

        this.telegramDAO = telegramDAO;

        decoration = new Decoration(this);


        decoration.setContentPanel(overlayDialog);


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
            switchFromPhone();

        });

        regForm.addActionListenerForSwitchAction(e -> {

            switchFromRegistration();
        });

        checkCodeForm.addActionListenerForSwitchAction(e -> {

            switchFromCode();
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

        mainForm.setContactsPanel(contactsLayeredPane);
        contactsLayeredPane.add(contactsList, new Integer(0));

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

        mainForm.addGearEventListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Me me = telegramProxy.getMe();
                ContactInfo contactInfo = Helper.toContactInfo(me, telegramProxy, false, false);
                profileForm.setContactInfo(contactInfo);
                changeOverlayPanel(profileForm);
            }
        });

        profileForm.addActionListenerForClose(actionEvent -> changeOverlayPanel(null));

        profileForm.addActionListenerForLogout(e -> switchToBegin());
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

    private void switchFromPhone() {
        String phoneNumber = mobileNumberForm.getFormattedValue();
        if (phoneNumber == null) {
            showPhoneNumberEmpty();
        } else {
            switchFromPhone(phoneNumber);
        }
    }

    private void switchFromPhone(String phoneNumber) {
        try {
            try {
                telegramDAO.acceptNumber(phoneNumber.replaceAll("[\\D]+", ""));

                if (telegramDAO.canSignUp()) {
                    if (showQuestionMessage("Пользователь не зарегистрирован. Будет регистрироваться?", "Внимание!")) {
                        showNameRequest(true);
                    } else {
                        showPhoneNumberRequest(true);
                        return;
                    }
                } else {
                    sendAndRequestCode();
                }

                checkCodeForm.setPhoneLabelText(phoneNumber);

            } catch (ApiException e) {
                if (e.isPhoneNumberInvalid()) {
                    showPhoneNumberInvalid();
                    return;
                }
                throw e;
            }
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void switchFromRegistration() {
        String firstName = regForm.getNameField();
        String lastName = regForm.getSurnameField();
        //Отсекаем только очевидный ляп.
        //С остальным пусть сервер разбирается
        if ((firstName == null || firstName.isEmpty())
                && (lastName == null || lastName.isEmpty())) {
            showNameInvalid();
        } else {
            try {
                sendAndRequestCode();
            } catch (Exception e) {
                catchException(e);
            }
        }
    }

    private void switchFromCode() {
        String firstName = regForm.getNameField();
        String lastName = regForm.getSurnameField();
        char[] code = checkCodeForm.getPasswordField();
        String codeToString = String.valueOf(code);
        if (codeToString.isEmpty()) {
            showCodeEmpty();
        } else {
            switchFromCode(firstName, lastName, codeToString);
        }
    }

    private void switchFromCode(String firstName, String lastName, String code) {
        try {
            try {
                if (telegramDAO.canSignIn())
                    telegramDAO.signIn(code);
                else
                    telegramDAO.signUp(code, firstName, lastName);
                switchToMainScreen();
            } catch (ApiException e) {
                if (e.isCodeInvalid()) {
                    showCodeInvalid();
                    return;
                }
                if (e.isCodeEmpty()) {
                    showCodeEmpty();
                    return;
                }
                if (e.isCodeExpired()) {
                    showCodeExpired();
                    return;
                }
                if (e.isNameInvalid()) {
                    showNameInvalid();
                    return;
                }
                throw e;
            }
        } catch (Exception e) {
            catchException(e);
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

    private void showPhoneNumberEmpty() {
        showWarningMessage("Введите корректный номер телефона!", "Внимание!");
        showPhoneNumberRequest(true);
    }

    private void showNameInvalid() {
        showWarningMessage("Неверные регистрационные данные", "Внимание!");
        showNameRequest(false);
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
        showCodeRequest();
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
        changeContentPanel(mobileNumberForm);
        if (clear)
            mobileNumberForm.clear();
        mobileNumberForm.transferFocusTo();
    }

    private void showNameRequest(boolean clear) {
        changeContentPanel(regForm);
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

    private void changeOverlayPanel(Container overlayPanel) {
        overlayDialog.setOverlayPanel(overlayPanel);
    }

    private void switchToMainScreen() throws ApiException {
        changeContentPanel(mainForm);
        createTelegramProxy();
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

    private void changeContentPanel(Container contentPanel) {
        overlayDialog.setContentPanel(contentPanel);
    }

    private void switchToBegin() {
        try {
            destroyTelegramProxy();
            changeOverlayPanel(null);
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

    private void showCodeRequest() {
        changeContentPanel(checkCodeForm);
        checkCodeForm.clear();
        checkCodeForm.transferFocusTo();
    }
}
