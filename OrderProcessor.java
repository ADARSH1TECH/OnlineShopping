package OnlineShopping;

// OrderProcessor.java
import java.sql.*;

public class OrderProcessor {
    public static boolean processOrder(String username, int productId, int quantity) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "password")) {
            PreparedStatement checkStock = con.prepareStatement("SELECT stock FROM products WHERE id=?");
            checkStock.setInt(1, productId);
            ResultSet rs = checkStock.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock >= quantity) {
                    PreparedStatement insertOrder = con.prepareStatement("INSERT INTO orders (username, product_id, quantity) VALUES (?, ?, ?)");
                    insertOrder.setString(1, username);
                    insertOrder.setInt(2, productId);
                    insertOrder.setInt(3, quantity);
                    insertOrder.executeUpdate();

                    PreparedStatement updateStock = con.prepareStatement("UPDATE products SET stock = stock - ? WHERE id = ?");
                    updateStock.setInt(1, quantity);
                    updateStock.setInt(2, productId);
                    updateStock.executeUpdate();

                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
