import frame.MyFrame;
import org.javagram.dao.DebugTelegramDAO;
import org.javagram.dao.TelegramDAO;

import javax.swing.*;

public class Loader {
    public static void main(String[] args) throws Exception {

//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (info.getName().equals("Nimbus")) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }

                    TelegramDAO telegramDAO =
                            new DebugTelegramDAO();
//                new ApiBridgeTelegramDAO(Config.SERVER, Config.APP_ID, Config.APP_HASH);

                    MyFrame frame = new MyFrame(telegramDAO);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}
