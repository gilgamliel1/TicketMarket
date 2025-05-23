-- Drop and recreate the TicketMarket database
DROP DATABASE IF EXISTS TicketMarket;
CREATE DATABASE TicketMarket CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE TicketMarket;

-- Drop existing tables if they exist to avoid conflicts
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

-- Create 'users' table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    user_first_name VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL,
    user_id_number VARCHAR(9) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    profile_picture_url VARCHAR(255),
    bio VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    wallet_balance INT DEFAULT 0
) ENGINE=InnoDB;

-- Create 'events' table
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    event_loc VARCHAR(255) NOT NULL,
    event_desc VARCHAR(255) DEFAULT NULL,
    event_owner VARCHAR(255) NOT NULL,
    event_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by_us BOOLEAN DEFAULT FALSE, -- Indicates if the event is system-generated
    created_by VARCHAR(255) NOT NULL DEFAULT 'system', -- Field to track who created the event
    ticket_max_price INT DEFAULT 1000, -- Field to store the maximum ticket price
    number_of_tickets INT DEFAULT 0, -- Field to store the total number of tickets
    tag VARCHAR(255) NOT NULL -- Field to categorize the event
) ENGINE=InnoDB;

-- Create 'tickets' table
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    seller_id INT NOT NULL,
    price INT NOT NULL,
    description VARCHAR(50),
    for_sale BOOLEAN DEFAULT FALSE,
    sold BOOLEAN DEFAULT FALSE,
    generated_by_us BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    serial_key VARCHAR(50) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Create 'transactions' table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price INT NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Insert example users (with unique IDs and emails)
INSERT INTO users (
    user_name, user_first_name, user_last_name, user_id_number,
    email, password_hash, profile_picture_url, bio, wallet_balance
) VALUES
('gil', 'gil', 'gamliel', '208151035', 'admin@example.com', '1234', 'https://example.com/profile1.jpg', 'System administrator', 100),
('shahar', 'shahar', 'agadi', '207848847', 'john@example.com', '1234', 'https://example.com/profile2.jpg', 'Event lover and party goer', 100),
('idan', 'Idan', 'Azama', '206915308', 'idan@example.com', '1234', 'https://example.com/profile3.jpg', 'Backend developer', 50),
('dror', 'Dror', 'Alon', '206000028', 'dror@example.com', '1234', 'https://example.com/profile4.jpg', 'DJ and event host', 75);

-- Insert example events with new fields
INSERT INTO events (
    event_name, event_date, event_loc, event_desc, event_owner, generated_by_us, tag, created_by, ticket_max_price, number_of_tickets
) VALUES
('House Party', '2025-05-25 22:00:00', 'Beer Sheva', 'Party at my house', 'System', FALSE, 'Parties', 'gil', 1000, 50),
('Live Coding Session', '2025-05-26 18:00:00', 'Ben-Gurion University, Building 28', 'An evening of real-time coding and pizza!', 'System', FALSE, 'Other', 'shahar', 1000, 30),
('Startup Meetup', '2025-05-27 19:00:00', 'Tel Aviv Hub', 'Networking for tech entrepreneurs', 'System', FALSE, 'Other', 'idan', 1000, 100),
('Beach Festival', '2025-06-28 16:00:00', 'Tel Aviv Beach', 'Music, food, and fun all evening!', 'System', FALSE, 'Concert', 'dror', 1000, 200);
