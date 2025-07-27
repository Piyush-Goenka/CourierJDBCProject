package dao;

import db.ConnectionManager;
import java.sql.*;

public class MetricsDAO {
    public void getAverageDeliveryTimeByRoute() {
        String sql = "SELECT origin_id, destination_id, " +
                     "AVG(TIMESTAMPDIFF(HOUR, created_time, actual_delivery)) AS avg_hours " +
                     "FROM shipments " +
                     "WHERE actual_delivery IS NOT NULL " +
                     "GROUP BY origin_id, destination_id";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Avg Hours: " + rs.getDouble("avg_hours"));
            }
            if (!found) {
                System.out.println("No data.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
