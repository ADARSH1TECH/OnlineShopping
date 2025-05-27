package OnlineShopping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean succeeded;

    public Login(Frame parent) {
        super(parent, "Login", true);
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        add(panel);
    }

    private void login() {
        String user = usernameField.getText();
        String pass = String.valueOf(passwordField.getPassword());

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                succeeded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getUsername() {
        return usernameField.getText();
    }
}
