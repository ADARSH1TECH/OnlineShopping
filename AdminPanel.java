package OnlineShopping;

// AdminPanel.java
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminPanel extends JFrame {
    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTable inventoryTable = ProductDisplay.getProductTable();
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JButton viewTransactions = new JButton("View Transactions");
        viewTransactions.addActionListener(e -> viewOrders());
        add(viewTransactions, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void viewOrders() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "password");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {

            JTable orderTable = new JTable(DbUtils.resultSetToTableModel(rs));
            JFrame frame = new JFrame("Transactions");
            frame.add(new JScrollPane(orderTable));
            frame.setSize(500, 400);
            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}