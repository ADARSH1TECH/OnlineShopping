package OnlineShopping;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PlaceOrder extends JFrame {
    private String username;

    public PlaceOrder(String username) {
        this.username = username;
        setTitle("Place Order Summary");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea orderDetails = new JTextArea();
        orderDetails.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDetails);
        add(scrollPane, BorderLayout.CENTER);

        double total = 0.0;

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT p.name, c.price FROM cart c " +
                    "JOIN products p ON c.product_id = p.id " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE u.username = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder("Order Summary:\n\n");
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                total += price;
                sb.append(name).append(" - $").append(price).append("\n");
            }

            sb.append("\nTotal: $").append(String.format("%.2f", total));
            orderDetails.setText(sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add "Confirm Order" button
        JButton confirmBtn = new JButton("Confirm Order");
        confirmBtn.addActionListener(e -> confirmOrder());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(confirmBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void confirmOrder() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to place the order?",
                "Confirm Order", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // You could also clear the cart here
            clearCart();
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
            this.dispose(); // close the window
        }
    }

    private void clearCart() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM cart WHERE user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
