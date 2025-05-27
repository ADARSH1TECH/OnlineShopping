package OnlineShopping;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginDialog = new Login(null);
            loginDialog.setVisible(true);

            if (loginDialog.isSucceeded()) {
                Menu menu = new Menu(loginDialog.getUsername());
                menu.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
