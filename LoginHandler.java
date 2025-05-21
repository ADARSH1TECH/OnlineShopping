package OnlineShopping;

// LoginHandler.java
import java.sql.*;

public class LoginHandler {
    public static String authenticate(String username, String password) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "@Babajii123");
             PreparedStatement pst = con.prepareStatement("SELECT role FROM users WHERE username=? AND password=?")) {

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

