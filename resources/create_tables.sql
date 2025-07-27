CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE agent (
    agent_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contacts VARCHAR(100)
);

CREATE TABLE location (
    location_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    parent_location_id BIGINT,
    pinCode VARCHAR(10),
    FOREIGN KEY (parent_location_id) REFERENCES location(location_id)
);

CREATE TABLE driver (
    driver_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    contact VARCHAR(100),
    `limit` BIGINT NOT NULL
);

CREATE TABLE shipments (
    shipment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT,
    recipient_id BIGINT,
    origin_id BIGINT,
    destination_id BIGINT,
    status ENUM('pending', 'in_transit', 'delivered', 'returned') NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    estimated_delivery_time DATETIME,
    actual_delivery DATETIME,
    FOREIGN KEY (sender_id) REFERENCES customer(customer_id),
    FOREIGN KEY (recipient_id) REFERENCES customer(customer_id),
    FOREIGN KEY (origin_id) REFERENCES location(location_id),
    FOREIGN KEY (destination_id) REFERENCES location(location_id)
);

CREATE TABLE package (
    package_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    shipment_id BIGINT,
    weight DECIMAL(10, 2),
    description VARCHAR(255),
    FOREIGN KEY (shipment_id) REFERENCES shipments(shipment_id)
);

CREATE TABLE driver_shipment_assignment (
    assignment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id BIGINT,
    shipment_id BIGINT,
    start_location_id BIGINT,
    end_location_id BIGINT,
    delivered BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (driver_id) REFERENCES driver(driver_id),
    FOREIGN KEY (shipment_id) REFERENCES shipments(shipment_id),
    FOREIGN KEY (start_location_id) REFERENCES location(location_id),
    FOREIGN KEY (end_location_id) REFERENCES location(location_id)
);

CREATE TABLE status_logs (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    shipment_id BIGINT,
    location_id BIGINT,
    agent_id BIGINT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments(shipment_id),
    FOREIGN KEY (location_id) REFERENCES location(location_id),
    FOREIGN KEY (agent_id) REFERENCES agent(agent_id)
);

CREATE INDEX idx_statuslogs_shipment ON status_logs (shipment_id);
CREATE INDEX idx_shipments_origin_created ON shipments (origin_id, created_time);
CREATE INDEX idx_shipments_origin_dest ON shipments (origin_id, destination_id);
CREATE INDEX idx_shipments_status ON shipments (status);
CREATE INDEX idx_driver_assignment ON driver_shipment_assignment (driver_id, delivered);

