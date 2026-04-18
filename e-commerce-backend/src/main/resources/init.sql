-- Database initialization script for E-Commerce Backend
-- Run this manually if you don't want Hibernate to auto-create the schema

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ecommerce_db;

-- Note: Hibernate will automatically create all tables when spring.jpa.hibernate.ddl-auto=update
-- This script is for reference or manual setup

-- Example: If you need to pre-populate some test data:

-- Insert test users
-- INSERT INTO users (username, email, password, first_name, last_name, created_at, updated_at)
-- VALUES
-- ('admin', 'admin@example.com', 'admin123', 'Admin', 'User', NOW(), NOW()),
-- ('testuser', 'test@example.com', 'test123', 'Test', 'User', NOW(), NOW());

-- Insert test products
-- INSERT INTO products (name, description, price, stock, created_at)
-- VALUES
-- ('iPhone 15', 'Apple iPhone 15 128GB', 799.00, 50, NOW()),
-- ('MacBook Pro', 'Apple MacBook Pro 14-inch M3', 1999.00, 20, NOW()),
-- ('AirPods Pro', 'Apple AirPods Pro 2nd Gen', 249.00, 100, NOW());
