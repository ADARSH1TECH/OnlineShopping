package OnlineShopping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class Menu extends JFrame {
    private String username;
    private JLabel totalLabel;
    private double totalPrice = 0.0;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private HashMap<JRadioButton, Product> productMap = new HashMap<>();

    public Menu(String username) {
        this.username = username;
        setTitle("Product Menu");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel productPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        add(scrollPane, BorderLayout.CENTER);

        loadProducts(productPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        totalLabel = new JLabel("Total Price: $0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel);

        JPanel buttonPanel = new JPanel();
        JButton buyNowBtn = new JButton("Buy Now");
        JButton addToCartBtn = new JButton("Add to Cart");
        JButton placeOrderBtn = new JButton("Place Order");

        buyNowBtn.addActionListener(e -> buyNow());
        addToCartBtn.addActionListener(e -> addToCart());
        placeOrderBtn.addActionListener(e -> openPlaceOrderWindow());

        buttonPanel.add(buyNowBtn);
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(placeOrderBtn);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadProducts(JPanel productPanel) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM products";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int productId = rs.getInt("id");
                String productName = rs.getString("name");
                double productPrice = rs.getDouble("price");

                JRadioButton radioBtn = new JRadioButton(productName + " - $" + productPrice);
                buttonGroup.add(radioBtn);
                productPanel.add(radioBtn);

                productMap.put(radioBtn, new Product(productId, productName, productPrice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Product getSelectedProduct() {
        for (JRadioButton btn : productMap.keySet()) {
            if (btn.isSelected()) {
                return productMap.get(btn);
            }
        }
        JOptionPane.showMessageDialog(this, "Please select a product.");
        return null;
    }

    private void buyNow() {
        Product selected = getSelectedProduct();
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                    "You selected to buy:\n" + selected.name + "\nPrice: $" + selected.price,
                    "Buy Now", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addToCart() {
        Product selected = getSelectedProduct();
        if (selected == null) return;

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO cart (user_id, product_id, price) " +
                    "SELECT id, ?, ? FROM users WHERE username=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, selected.id);
            stmt.setDouble(2, selected.price);
            stmt.setString(3, username);
            stmt.executeUpdate();

            totalPrice += selected.price;
            totalLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
            JOptionPane.showMessageDialog(this, "Product added to cart.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openPlaceOrderWindow() {
        new PlaceOrder(username);
    }

    class Product {
        int id;
        String name;
        double price;

        Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }
}
