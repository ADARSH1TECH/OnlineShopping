package OnlineShopping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    JTextField userField;
    JPasswordField passField;

    public LoginForm() {
        setTitle("Login");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load login image
        ImageIcon icon = new ImageIcon("login_image.jpg");
        Image scaledImage = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.NORTH);

        // Login form panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        userField = new JTextField(15);
        panel.add(userField);

        panel.add(new JLabel("Password:"));
        passField = new JPasswordField(15);
        panel.add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticate());
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void authenticate() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        String role = LoginHandler.authenticate(username, password);

        if (role != null) {
            dispose();
            if (role.equals("admin")) new AdminPanel();
            else new CustomerPanel(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }
}
