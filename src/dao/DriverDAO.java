package dao;

import db.ConnectionManager;
import java.sql.*;

public class DriverDAO {

    public void getPendingShipments(long driverId) {
        String sql = "SELECT s.shipment_id, s.origin_id, s.destination_id, s.status, s.created_time " +
                     "FROM shipments s " +
                     "JOIN driver_shipment_assignment dsa ON s.shipment_id = dsa.shipment_id " +
                     "WHERE dsa.driver_id = ? AND s.status = 'pending'";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, driverId);
            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Shipment ID: " + rs.getLong("shipment_id"));
            }

            if (!found) {
                System.out.println(" No pending shipments.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
