package fake;

import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;
import org.javagram.response.object.UserContact;
import org.telegram.api.TLUserContact;
import org.telegram.api.TLUserSelf;
import org.telegram.api.auth.TLAuthorization;
import org.telegram.api.engine.RpcException;

import java.io.IOException;
import java.util.ArrayList;

public class TelegramApiBridge {
    public TelegramApiBridge() throws IOException {

    }

    private static final String REGISTERED_NUMBER = "+71111111111";
    private static final String UNREGISTERED_NUMBER = "+70000000000";
    private static final String INVITED_NUMBER = "+72222222222";

    private static final String AUTH_CODE = "00000";

    private String currentPhone;
    private boolean loggedIn;

    public AuthCheckedPhone authCheckPhone(String phoneNumber) throws IOException {
        if (phoneNumber.equals(REGISTERED_NUMBER)) {
            return new AuthCheckedPhone(true, false);
        } else if (phoneNumber.equals(UNREGISTERED_NUMBER)) {
            return new AuthCheckedPhone(false, false);
        } else if (phoneNumber.equals(INVITED_NUMBER)) {
            return new AuthCheckedPhone(false, true);
        } else {
            throw new RpcException(0, "PHONE_NUMBER_INVALID");
        }
    }

    public AuthSentCode authSendCode(String phoneNumber) throws IOException {
        if (loggedIn)
            throw new RpcException(0, "INVALID_OPERATION");
        AuthCheckedPhone authCheckedPhone = authCheckPhone(phoneNumber);
        currentPhone = phoneNumber;
        return new AuthSentCode(authCheckedPhone.isRegistered(), "");

    }

    public AuthAuthorization authSignIn(String smsCode) throws IOException {
        return authSignOnUp(false, smsCode, "John", "Doe");
    }

    public AuthAuthorization authSignUp(String smsCode, String firstName, String lastName) throws IOException {
        return authSignOnUp(true, smsCode, firstName, lastName);
    }

    private AuthAuthorization authSignOnUp(boolean up, String smsCode, String firstName, String lastName) throws IOException {
        if (loggedIn)
            throw new RpcException(0, "INVALID_OPERATION");

        if (smsCode.equals(AUTH_CODE) && currentPhone != null) {
            if (authCheckPhone(currentPhone).isRegistered() != up) {
                TLUserSelf user = new TLUserSelf(1, firstName,
                        lastName, currentPhone, null, null, true);
                loggedIn = true;
                return new AuthAuthorization(new TLAuthorization(0, user));
            } else if (up) {
                throw new RpcException(0, "INVALID_OPERATION");
            } else {
                throw new RpcException(0, "PHONE_NUMBER_UNOCCUPIED");
            }

        } else if (currentPhone == null) {
            throw new RpcException(0, "PHONE_NUMBER_INVALID");
        } else {
            throw new RpcException(0, "INVALID_CODE");
        }
    }

    public ArrayList<UserContact> contactsGetContacts() throws IOException {
        ArrayList<UserContact> list = new ArrayList<>();
        list.add(new UserContact(new TLUserContact(2, "Jane", "Doe", 0,
                "+79999999999", null, null)));
        list.add(new UserContact(new TLUserContact(3, "Jane", "Doe", 0,
                "+78888888888", null, null)));
        return list;
    }

    public boolean authLogOut() throws IOException {
        if (loggedIn) {
            loggedIn = false;
            return true;
        } else {
            return false;
        }
    }

    public void close() throws IOException {
        authLogOut();
    }
}
