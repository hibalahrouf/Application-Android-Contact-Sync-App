CREATE DATABASE IF NOT EXISTS contactsync_lab
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE contactsync_lab;

CREATE TABLE IF NOT EXISTS contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    source VARCHAR(50) DEFAULT 'android_device',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
