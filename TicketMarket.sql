CREATE DATABASE TicketMarket;

USE TicketMarket;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE,
    user_first_name VARCHAR(50)  ,
    user_last_name VARCHAR(50)  ,
    user_id_number VARCHAR(9)  UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255) ,
    profile_picture_url VARCHAR(255),
    bio VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    wallet_balance INT DEFAULT(0)
);

CREATE TABLE events (
event_id INT AUTO_INCREMENT PRIMARY KEY ,
event_name VARCHAR(100) NOT NULL  , 
event_date TIMESTAMP NOT NULL , 
event_loc VARCHAR(255) NOT NULL ,
event_desc VARCHAR(255) DEFAULT(NULL) ,  
event_owner VARCHAR(255)  NOT NULL ,
event_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP , 
tag VARCHAR(255) NOT NULL
);

CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY, 
    event_id INT NOT NULL,
    seller_id INT NOT NULL,
    price INT NOT NULL,
    description VARCHAR(50),
    status ENUM('available', 'sold') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    serial_key VARCHAR(50) NOT NULL , 
    FOREIGN KEY (event_id) REFERENCES Events(event_id),
    FOREIGN KEY (seller_id) REFERENCES Users(user_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price INT NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES Tickets(ticket_id),
    FOREIGN KEY (buyer_id) REFERENCES Users(user_id),
    FOREIGN KEY (seller_id) REFERENCES Users(user_id)
);

