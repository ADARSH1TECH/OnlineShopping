package OnlineShopping;

// CustomerPanel.java
import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JFrame {
    private String username;
    private JTable productTable;
    private JTextField productIdField, quantityField;

    public CustomerPanel(String username) {
        this.username = username;
        setTitle("Customer Panel");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        productTable = ProductDisplay.getProductTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel orderPanel = new JPanel();
        orderPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField(5);
        orderPanel.add(productIdField);

        orderPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        orderPanel.add(quantityField);

        JButton orderButton = new JButton("Place Order");
        orderButton.addActionListener(e -> placeOrder());
        orderPanel.add(orderButton);

        add(orderPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void placeOrder() {
        int productId = Integer.parseInt(productIdField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        boolean success = OrderProcessor.processOrder(username, productId, quantity);
        if (success) {
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
            productTable.setModel(ProductDisplay.getProductTable().getModel());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to place order. Check stock.");
        }
    }
}