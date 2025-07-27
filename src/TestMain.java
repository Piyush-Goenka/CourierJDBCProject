import db.ConnectionManager;
import java.sql.Connection;

public class TestMain {
    public static void main(String[] args) {
        try (Connection conn = ConnectionManager.getConnection()) {
            System.out.println("Connection test successful!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}