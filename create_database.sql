CREATE DATABASE IF NOT EXISTS user_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'user';
GRANT ALL PRIVILEGES ON user_manager.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
