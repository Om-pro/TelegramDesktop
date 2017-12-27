package helpers;


import java.awt.*;
import java.io.InputStream;

/**
 * Created by HerrSergio on 15.06.2016.
 */
//https://lingualift.com/blog/best-free-cyrillic-fonts/
public class Fonts {
    private static Font nameFont;

    //https://www.behance.net/gallery/Days-typeface/190108
    public static Font getNameFont() {
        if (nameFont == null)
            nameFont = loadFont("OpenSansRegular.ttf");
        return nameFont;
    }

    private static Font loadFont(String name) {
        try(InputStream inputStream = Fonts.class.getResourceAsStream("/fonts/" + name)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("serif", Font.PLAIN, 24);
        }
    }
}
