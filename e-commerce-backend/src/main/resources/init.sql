-- Database initialization script for E-Commerce Backend
-- Creates all tables matching the entity mappings

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ecommerce_db;

-- Create tables manually (if you don't want Hibernate to auto-create)

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(50),
    country VARCHAR(50),
    is_default BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT,
    image_url VARCHAR(255),
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20),
    total_amount DECIMAL(10, 2),
    user_id BIGINT NOT NULL,
    shipping_address_id BIGINT NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (shipping_address_id) REFERENCES addresses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    price DECIMAL(10, 2),
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User wishlist join table (ManyToMany between users and products)
CREATE TABLE IF NOT EXISTS user_wishlist (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Quartz Scheduler tables (Spring Boot/Quartz can auto-create these with spring.quartz.jdbc.initialize-schema=always)
-- If you want to create them manually:
-- https://github.com/quartz-scheduler/quartz/blob/master/docs/table_docsSQL.adoc

-- Spring Batch tables (Spring Batch can auto-create these)
-- https://docs.spring.io/spring-batch/reference/schema.html

-- Pre-populate test data (uncomment to use):

-- INSERT INTO users (username, email, password, first_name, last_name, created_at, updated_at)
-- VALUES
-- ('admin', 'admin@example.com', 'admin123', 'Admin', 'User', NOW(), NOW()),
-- ('testuser', 'test@example.com', 'test123', 'Test', 'User', NOW(), NOW());

-- INSERT INTO addresses (street, city, state, zip_code, country, is_default, user_id)
-- VALUES
-- ('123 Main St', 'New York', 'NY', '10001', 'USA', TRUE, 1);

-- INSERT INTO products (name, description, price, stock, created_at)
-- VALUES
-- ('iPhone 15', 'Apple iPhone 15 128GB', 799.00, 50, NOW()),
-- ('MacBook Pro', 'Apple MacBook Pro 14-inch M3', 1999.00, 20, NOW()),
-- ('AirPods Pro', 'Apple AirPods Pro 2nd Gen', 249.00, 100, NOW());

