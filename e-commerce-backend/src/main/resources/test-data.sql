-- Test Data for E-Commerce Backend
-- Pre-populated data for development and testing
-- Run this after tables are created

USE ecommerce_db;

-- Insert test users
INSERT INTO users (username, email, password, first_name, last_name, created_at, updated_at)
VALUES
('admin', 'admin@example.com', 'admin123', 'Admin', 'User', NOW(), NOW()),
('john.doe', 'john@example.com', 'password123', 'John', 'Doe', NOW(), NOW()),
('jane.smith', 'jane@example.com', 'smith456', 'Jane', 'Smith', NOW(), NOW()),
('mike.wilson', 'mike@example.com', 'wilson789', 'Mike', 'Wilson', NOW(), NOW());

-- Insert addresses for users
INSERT INTO addresses (street, city, state, zip_code, country, is_default, user_id)
VALUES
('123 Main Street', 'New York', 'NY', '10001', 'USA', TRUE, 1),
('456 Oak Avenue', 'Brooklyn', 'NY', '11201', 'USA', TRUE, 2),
('789 Pine Road', 'San Francisco', 'CA', '94107', 'USA', TRUE, 3),
('321 Cedar Lane', 'Seattle', 'WA', '98101', 'USA', TRUE, 4),
('100 Downtown St', 'New York', 'NY', '10002', 'USA', FALSE, 2);

-- Insert products
INSERT INTO products (name, description, price, stock, created_at)
VALUES
('iPhone 15', 'Apple iPhone 15 128GB - Black', 799.00, 45, NOW()),
('iPhone 15 Pro', 'Apple iPhone 15 Pro 256GB - Natural Titanium', 999.00, 30, NOW()),
('MacBook Pro 14"', 'Apple MacBook Pro 14-inch M3 Pro', 1999.00, 15, NOW()),
('MacBook Air 13"', 'Apple MacBook Air 13-inch M2', 1099.00, 25, NOW()),
('AirPods Pro 2nd Gen', 'Apple AirPods Pro 2nd Generation with MagSafe Charging', 249.00, 80, NOW()),
('AirPods Max', 'Apple AirPods Max Over-Ear Headphones - Silver', 549.00, 20, NOW()),
('iPad Pro 12.9"', 'Apple iPad Pro 12.9-inch M2 Chip 256GB', 1099.00, 35, NOW()),
('iPad Air', 'Apple iPad Air 10.9-inch 6th Generation 128GB', 599.00, 40, NOW()),
('Apple Watch Series 9', 'Apple Watch Series 9 41mm GPS Aluminum Case', 399.00, 50, NOW()),
('Apple Watch Ultra 2', 'Apple Watch Ultra 2 49mm Titanium Case', 799.00, 18, NOW()),
('Samsung Galaxy S24', 'Samsung Galaxy S24 128GB - Phantom Black', 799.99, 40, NOW()),
('Samsung Galaxy Book 4', 'Samsung Galaxy Book 4 15.6" Core i5', 999.99, 22, NOW()),
('Sony WH-1000XM5', 'Sony Wireless Noise Cancelling Headphones', 398.00, 35, NOW()),
('Bose QuietComfort 45', 'Bose Wireless Noise Cancelling Headphones', 329.00, 45, NOW());

-- Add some sample orders
INSERT INTO orders (order_number, status, total_amount, user_id, shipping_address_id, created_at)
VALUES
('ORD-ABC1234', 'DELIVERED', 1048.00, 2, 2, NOW() - INTERVAL 7 DAY),
('ORD-DEF5678', 'SHIPPED', 1248.99, 3, 3, NOW() - INTERVAL 3 DAY),
('ORD-GHI9012', 'PAID', 799.00, 4, 4, NOW() - INTERVAL 1 DAY),
('ORD-JKL3456', 'PENDING', 1147.99, 2, 2, NOW());

-- Add order items
INSERT INTO order_items (quantity, price, order_id, product_id)
VALUES
(1, 799.00, 1, 1),
(1, 249.00, 1, 5),
(1, 999.99, 2, 11),
(1, 249.00, 2, 5),
(1, 799.00, 3, 1),
(1, 599.00, 4, 8),
(1, 399.00, 4, 9),
(1, 149.99, 4, 13);

-- Add some items to wishlist
INSERT INTO user_wishlist (user_id, product_id)
VALUES
(2, 2),
(2, 3),
(3, 1),
(3, 14),
(4, 4),
(4, 10);
