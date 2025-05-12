-- Drop and recreate the TicketMarket database
DROP DATABASE IF EXISTS TicketMarket;
CREATE DATABASE TicketMarket;
USE TicketMarket;

-- Drop existing tables if they exist to avoid conflicts
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

-- Create 'users' table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE,
    user_first_name VARCHAR(50),
    user_last_name VARCHAR(50),
    user_id_number VARCHAR(9) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    profile_picture_url VARCHAR(255),
    bio VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    wallet_balance INT DEFAULT 0
);

-- Create 'events' table
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    event_loc VARCHAR(255) NOT NULL,
    event_desc VARCHAR(255) DEFAULT NULL,
    event_owner VARCHAR(255) NOT NULL,
    event_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tag VARCHAR(255) NOT NULL
);

-- Create 'tickets' table
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    seller_id INT NOT NULL,
    price INT NOT NULL,
    description VARCHAR(50),
    status ENUM('for_sale', 'not_for_sale') DEFAULT 'for_sale',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    serial_key VARCHAR(50) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

-- Create 'transactions' table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price INT NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id),
    FOREIGN KEY (buyer_id) REFERENCES users(user_id),
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

-- Insert example users (with unique IDs and emails)
INSERT INTO users (
    user_name, user_first_name, user_last_name, user_id_number,
    email, password_hash, profile_picture_url, bio, wallet_balance
) VALUES
('gil', 'gil', 'gamliel', '123456789', 'admin@example.com', '1234', 'https://example.com/profile1.jpg', 'System administrator', 100),
('shahar', 'shahar', 'agadi', '987654321', 'john@example.com', '1234', 'https://example.com/profile2.jpg', 'Event lover and party goer', 100),
('idan', 'Idan', 'Azama', '111222333', 'idan@example.com', '1234', 'https://example.com/profile3.jpg', 'Backend developer', 50),
('dror', 'Dror', 'Alon', '444555666', 'dror@example.com', '1234', 'https://example.com/profile4.jpg', 'DJ and event host', 75);

-- Insert 4 example events
INSERT INTO events (
    event_name, event_date, event_loc, event_desc, event_owner, tag
) VALUES
('House Party', '2025-05-02 22:00:00', 'Beer Sheva', 'Party at my house', 'gil', 'Parties'),
('Live Coding Session', '2025-05-10 18:00:00', 'Ben-Gurion University, Building 28', 'An evening of real-time coding and pizza!', 'shahar', 'Other'),
('Startup Meetup', '2025-05-18 19:00:00', 'Tel Aviv Hub', 'Networking for tech entrepreneurs', 'idan', 'Business'),
('Beach Festival', '2025-06-01 16:00:00', 'Tel Aviv Beach', 'Music, food, and fun all evening!', 'dror', 'Concert');
