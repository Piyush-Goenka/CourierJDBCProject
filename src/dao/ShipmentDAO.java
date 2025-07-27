package dao;

import db.ConnectionManager;
import java.sql.*;

public class ShipmentDAO {
    public void createShipment(long senderId, long recipientId, long originId, long destinationId, Timestamp estimatedDeliveryTime) {
        String sql = "INSERT INTO shipments (sender_id, recipient_id, origin_id, destination_id, status, estimated_delivery_time) " +
                     "VALUES (?, ?, ?, ?, 'pending', ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, senderId);
            stmt.setLong(2, recipientId);
            stmt.setLong(3, originId);
            stmt.setLong(4, destinationId);
            stmt.setTimestamp(5, estimatedDeliveryTime);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Shipment created successfully.");
            } else {
                System.out.println("No shipment was created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getShipmentStatus(long shipmentId) {
        String sql = "SELECT s.status, l.name AS location, a.name AS agent, sl.timestamp " +
                     "FROM shipments s " +
                     "JOIN status_logs sl ON s.shipment_id = sl.shipment_id " +
                     "JOIN location l ON sl.location_id = l.location_id " +
                     "JOIN agent a ON sl.agent_id = a.agent_id " +
                     "WHERE s.shipment_id = ? " +
                     "ORDER BY sl.timestamp DESC";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, shipmentId);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Status: " + rs.getString("status") +
                                   ", Location: " + rs.getString("location") +
                                   ", Agent: " + rs.getString("agent") +
                                   ", Time: " + rs.getTimestamp("timestamp"));
            }

            if (!found) {
                System.out.println("No status logs.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getDelayedShipments() {
        String sql = "SELECT shipment_id, estimated_delivery_time, actual_delivery " +
                     "FROM shipments " +
                     "WHERE actual_delivery IS NOT NULL AND actual_delivery > estimated_delivery_time";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Shipment ID: " + rs.getLong("shipment_id"));
            }

            if (!found) {
                System.out.println("No delayed shipment.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getDailyShipmentVolumeByOrigin() {
        String sql = "SELECT l.name AS origin_location, DATE(s.created_time) AS shipment_date, COUNT(*) AS volume " +
                    "FROM shipments s " +
                    "JOIN location l ON s.origin_id = l.location_id " +
                    "GROUP BY l.name, DATE(s.created_time) " +
                    "ORDER BY shipment_date DESC";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Origin: " + rs.getString("origin_location") +
                                ", Date: " + rs.getDate("shipment_date") +
                                ", Volume: " + rs.getInt("volume"));
            }

            if (!found) {
                System.out.println("No shipments found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
