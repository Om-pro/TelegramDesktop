package helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.*;

/**
 * Created by HerrSergio on 03.07.2016.
 */
public class PhoneNumberDocumentFilter extends DocumentFilter {

    private String regex = "\\+?\\d*";

    @Override
    public void remove(FilterBypass filterBypass, int i, int i1) throws BadLocationException {
        if(tryRemove(getText(filterBypass), i, i1).matches(regex)) {
            super.remove(filterBypass, i, i1);
        } else {
            beep();
        }
    }

    @Override
    public void insertString(FilterBypass filterBypass, int i, String s, AttributeSet attributeSet) throws BadLocationException {
        if(tryInsert(getText(filterBypass), i, s).matches(regex)) {
            super.insertString(filterBypass, i, s, attributeSet);
        } else {
            beep();
        }
    }

    @Override
    public void replace(FilterBypass filterBypass, int i, int i1, String s, AttributeSet attributeSet) throws BadLocationException {
        if(tryReplace(getText(filterBypass), i, i1, s).matches(regex)) {
            super.replace(filterBypass, i, i1, s, attributeSet);
        } else {
            beep();
        }
    }

    private String tryRemove(String text, int pos, int length) {
        return tryReplace(text, pos, length, "");
    }

    private String tryInsert(String text, int pos, String data)  {
        return tryReplace(text, pos, 0, data);
    }

    private String tryReplace(String text, int pos, int length, String data)  {
        return text.substring(0, pos) + data + text.substring(pos + length);
    }

    private String getText(FilterBypass filterBypass) throws BadLocationException {
        Document document = filterBypass.getDocument();
        int length = document.getLength();
        String text = document.getText(0, length);
        return text;

    }

    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
