package OnlineShopping;

// ProductDisplay.java
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.sql.*;

public class ProductDisplay {
    public static JTable getProductTable() {
        JTable table = new JTable();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "password");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return table;
    }
}
