package overlays;

import java.awt.image.BufferedImage;

public class ContactInfo {
    private String firstName;
    private String lastName;
    private String phone;
    private int id;
    private BufferedImage photo;

    public ContactInfo() {
        this("", "", "");
    }

    public ContactInfo(String phone, String firstName, String lastName) {
        this(phone, firstName, lastName, 0);
    }

    public ContactInfo(String phone, String firstName, String lastName, int id) {
        this(phone, firstName, lastName, id, null);
    }

    public ContactInfo(String phone, String firstName, String lastName, int id, BufferedImage photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.id = id;
        this.photo = photo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClearedPhone() {
        return getPhone().replaceAll("\\D+", "");
    }

    public BufferedImage getPhoto() {
        return photo;
    }

    public void setPhoto(BufferedImage photo) {
        this.photo = photo;
    }
}
