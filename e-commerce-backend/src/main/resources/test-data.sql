-- Test Data for E-Commerce Backend
-- Pre-populated data for development and testing
-- Run this after tables are created

USE ecommerce_db;

-- Insert test users
INSERT INTO users (username, email, password, first_name, last_name, created_at, updated_at)
VALUES
('admin', 'admin@example.com', 'admin123', 'Admin', 'User', NOW(), NOW()),
('john.doe', 'john@example.com', 'password123', 'John', 'Doe', NOW() - INTERVAL 30 DAY', NOW() - INTERVAL 30 DAY),
('jane.smith', 'jane@example.com', 'smith456', 'Jane', 'Smith', NOW() - INTERVAL 25 DAY', NOW() - INTERVAL 25 DAY),
('mike.wilson', 'mike@example.com', 'wilson789', 'Mike', 'Wilson', NOW() - INTERVAL 20 DAY', NOW() - INTERVAL 20 DAY),
('sarah.connor', 'sarah@example.com', 'sarah890', 'Sarah', 'Connor', NOW() - INTERVAL 15 DAY', NOW() - INTERVAL 15 DAY),
('david.brown', 'david@example.com', 'davidbrown', 'David', 'Brown', NOW() - INTERVAL 10 DAY', NOW() - INTERVAL 10 DAY),
('emily.davis', 'emily@example.com', 'emilydavis', 'Emily', 'Davis', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY);

-- Insert addresses for users
INSERT INTO addresses (street, city, state, zip_code, country, is_default, user_id)
VALUES
('123 Main Street', 'New York', 'NY', '10001', 'USA', TRUE, 1),
('456 Oak Avenue', 'Brooklyn', 'NY', '11201', 'USA', TRUE, 2),
('10 Park Place', 'New York', 'NY', '10002', 'USA', FALSE, 2),
('789 Pine Road', 'San Francisco', 'CA', '94107', 'USA', TRUE, 3),
('256 Market St', 'San Francisco', 'CA', '94105', 'USA', FALSE, 3),
('321 Cedar Lane', 'Seattle', 'WA', '98101', 'USA', TRUE, 4),
('500 Lakeview Dr', 'Bellevue', 'WA', '98004', 'USA', FALSE, 4),
('77 Ocean Blvd', 'Los Angeles', 'CA', '90001', 'USA', TRUE, 5),
('88 Mountain View', 'Chicago', 'IL', '60601', 'USA', TRUE, 6),
('10 Fifth Avenue', 'New York', 'NY', '10010', 'USA', TRUE, 7),
('15 Broadway', 'Manhattan', 'NY', '10005', 'USA', FALSE, 7);

-- Insert more products
INSERT INTO products (name, description, price, stock, created_at)
VALUES
('iPhone 15', 'Apple iPhone 15 128GB - Black', 799.00, 45, NOW() - INTERVAL 60 DAY),
('iPhone 15 Pro', 'Apple iPhone 15 Pro 256GB - Natural Titanium', 999.00, 30, NOW() - INTERVAL 60 DAY),
('iPhone 15 Plus', 'Apple iPhone 15 Plus 256GB - Blue', 899.00, 25, NOW() - INTERVAL 60 DAY),
('iPhone 14', 'Apple iPhone 14 128GB - Midnight', 699.00, 50, NOW() - INTERVAL 60 DAY),
('MacBook Pro 14"', 'Apple MacBook Pro 14-inch M3 Pro', 1999.00, 15, NOW() - INTERVAL 50 DAY),
('MacBook Pro 16"', 'Apple MacBook Pro 16-inch M3 Max', 2799.00, 10, NOW() - INTERVAL 50 DAY),
('MacBook Air 13"', 'Apple MacBook Air 13-inch M2', 1099.00, 25, NOW() - INTERVAL 50 DAY),
('MacBook Air 15"', 'Apple MacBook Air 15-inch M2', 1299.00, 20, NOW() - INTERVAL 50 DAY),
('AirPods Pro 2nd Gen', 'Apple AirPods Pro 2nd Generation with MagSafe Charging', 249.00, 80, NOW() - INTERVAL 40 DAY),
('AirPods 3rd Gen', 'Apple AirPods 3rd Generation Lightning Charging Case', 169.00, 100, NOW() - INTERVAL 40 DAY),
('AirPods Max', 'Apple AirPods Max Over-Ear Headphones - Silver', 549.00, 20, NOW() - INTERVAL 40 DAY),
('iPad Pro 12.9"', 'Apple iPad Pro 12.9-inch M2 Chip 256GB', 1099.00, 35, NOW() - INTERVAL 30 DAY),
('iPad Pro 11"', 'Apple iPad Pro 11-inch M2 Chip 128GB', 799.00, 45, NOW() - INTERVAL 30 DAY),
('iPad Air', 'Apple iPad Air 10.9-inch 6th Generation 128GB', 599.00, 40, NOW() - INTERVAL 30 DAY),
('iPad mini', 'Apple iPad mini 8.3-inch 6th Generation 64GB', 499.00, 55, NOW() - INTERVAL 30 DAY),
('Apple Watch Series 9', 'Apple Watch Series 9 41mm GPS Aluminum Case', 399.00, 50, NOW() - INTERVAL 20 DAY),
('Apple Watch Series 9 45mm', 'Apple Watch Series 9 45mm GPS Aluminum Case', 429.00, 45, NOW() - INTERVAL 20 DAY),
('Apple Watch Ultra 2', 'Apple Watch Ultra 2 49mm Titanium Case', 799.00, 18, NOW() - INTERVAL 20 DAY),
('Samsung Galaxy S24', 'Samsung Galaxy S24 128GB - Phantom Black', 799.99, 40, NOW() - INTERVAL 20 DAY),
('Samsung Galaxy S24 Ultra', 'Samsung Galaxy S24 Ultra 256GB - Titanium Black', 1199.99, 25, NOW() - INTERVAL 20 DAY),
('Samsung Galaxy Book 4', 'Samsung Galaxy Book 4 15.6" Core i5', 999.99, 22, NOW() - INTERVAL 15 DAY),
('Samsung Galaxy Book 4 Pro', 'Samsung Galaxy Book 4 Pro 14" Core i7', 1499.99, 12, NOW() - INTERVAL 15 DAY),
('Sony WH-1000XM5', 'Sony Wireless Noise Cancelling Headphones Black', 398.00, 35, NOW() - INTERVAL 15 DAY),
('Sony WH-CH720N', 'Sony Wireless Noise Cancelling Headphones Blue', 149.99, 60, NOW() - INTERVAL 15 DAY),
('Bose QuietComfort 45', 'Bose Wireless Noise Cancelling Headphones Triple Black', 329.00, 45, NOW() - INTERVAL 10 DAY),
('Bose Sport Earbuds', 'Bose True Wireless Bluetooth Earbuds', 199.00, 55, NOW() - INTERVAL 10 DAY),
('Google Pixel 8', 'Google Pixel 8 128GB - Obsidian', 699.00, 38, NOW() - INTERVAL 10 DAY),
('Google Pixel Watch 2', 'Google Pixel Watch 2 41mm Wi-Fi', 349.00, 30, NOW() - INTERVAL 10 DAY),
('OnePlus 12', 'OnePlus 12 256GB - Silky Black', 799.00, 28, NOW() - INTERVAL 5 DAY),
('Nothing Phone (2)', 'Nothing Phone (2) 128GB - Dark Gray', 599.00, 20, NOW() - INTERVAL 5 DAY);

-- Add more sample orders from different users
INSERT INTO orders (order_number, status, total_amount, user_id, shipping_address_id, created_at)
VALUES
('ORD-ABC1234', 'DELIVERED', 1048.00, 2, 2, NOW() - INTERVAL 60 DAY),
('ORD-DEF5678', 'SHIPPED', 1248.99, 3, 3, NOW() - INTERVAL 45 DAY),
('ORD-GHI9012', 'PAID', 799.00, 4, 4, NOW() - INTERVAL 30 DAY),
('ORD-JKL3456', 'DELIVERED', 1147.99, 5, 8, NOW() - INTERVAL 25 DAY),
('ORD-MNO6789', 'DELIVERED', 568.00, 6, 9, NOW() - INTERVAL 20 DAY),
('ORD-PQR0123', 'PAID', 948.99, 7, 10, NOW() - INTERVAL 15 DAY),
('ORD-STU4567', 'SHIPPED', 1299.00, 5, 8, NOW() - INTERVAL 10 DAY),
('ORD-VWX8901', 'PAID', 429.00, 3, 4, NOW() - INTERVAL 8 DAY),
('ORD-YZA2345', 'DELIVERED', 1048.98, 2, 3, NOW() - INTERVAL 5 DAY),
('ORD-BCD6789', 'PENDING', 699.99, 7, 11, NOW() - INTERVAL 2 DAY),
('ORD-EFG0123', 'PROCESSING', 898.99, 6, 9, NOW() - INTERVAL 1 DAY);

-- Add order items for all orders
-- Order 1 (user 2)
INSERT INTO order_items (quantity, price, order_id, product_id)
VALUES
(1, 799.00, 1, 1),
(1, 249.00, 1, 9),
-- Order 2 (user 3)
(1, 999.99, 2, 19),
(1, 249.00, 2, 9),
-- Order 3 (user 4)
(1, 799.00, 3, 1),
-- Order 4 (user 5)
(1, 1099.00, 4, 12),
(1, 49.99, 4, 24),
-- Order 5 (user 6)
(1, 399.00, 5, 16),
(1, 169.00, 5, 10),
-- Order 6 (user 7)
(1, 699.00, 6, 27),
(1, 249.99, 6, 26),
-- Order 7 (user 5)
(1, 1299.00, 7, 6),
-- Order 8 (user 3)
(1, 429.00, 8, 16),
-- Order 9 (user 2)
(1, 699.00, 9, 4),
(1, 349.98, 9, 18),
-- Order 10 (user 7)
(1, 699.99, 10, 27),
-- Order 11 (user 6)
(1, 599.00, 11, 13),
(1, 299.99, 11, 28);

-- Add more wishlist entries for all users
INSERT INTO user_wishlist (user_id, product_id)
VALUES
-- User 2 (john.doe)
(2, 2),
(2, 3),
(2, 6),
(2, 15),
-- User 3 (jane.smith)
(3, 1),
(3, 14),
(3, 20),
(3, 25),
-- User 4 (mike.wilson)
(4, 4),
(4, 10),
(4, 17),
-- User 5 (sarah.connor)
(5, 5),
(5, 8),
(5, 18),
(5, 29),
-- User 6 (david.brown)
(6, 1),
(6, 19),
(6, 22),
-- User 7 (emily.davis)
(7, 1),
(7, 12),
(7, 21),
(7, 23);
