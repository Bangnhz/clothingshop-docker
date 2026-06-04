DROP SCHEMA IF EXISTS fashion_shop;
CREATE SCHEMA fashion_shop;
USE fashion_shop;


-- USERS

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    full_name VARCHAR(255),
    phone VARCHAR(20) UNIQUE,
    role VARCHAR(50) DEFAULT 'customer',
    discount_percent DECIMAL(5,2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- CATEGORIES

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);


-- SIZES

CREATE TABLE sizes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);


-- PRODUCTS

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DECIMAL(12,2),
    created_at TIMESTAMP,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);


-- PRODUCT IMAGES

CREATE TABLE product_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255),
    is_main BOOLEAN,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id)
);


-- PRODUCT VARIANTS

CREATE TABLE product_variants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    stock_quantity INT,
    product_id INT,
    size_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (size_id) REFERENCES sizes(id)
);


-- ADDRESSES

CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address_line VARCHAR(255),
    city VARCHAR(255),
    district VARCHAR(255),
    ward VARCHAR(255),
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- CARTS

CREATE TABLE carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_price DECIMAL(12,2) DEFAULT 0,
    created_at TIMESTAMP,
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- CART ITEMS

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    cart_id INT,
    product_variant_id INT,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (product_variant_id) REFERENCES product_variants(id)
);


-- ORDERS

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    is_guest BOOLEAN DEFAULT TRUE,
    email VARCHAR(255),
    phone VARCHAR(20),
    subtotal DECIMAL(12,2),
    discount_amount DECIMAL(12,2) DEFAULT 0,
    shipping_fee DECIMAL(12,2) DEFAULT 0,
    total_price DECIMAL(12,2),
    voucher_code VARCHAR(100) NULL,
    order_status VARCHAR(50) DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- ORDER ITEMS

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(12,2),
    quantity INT,
    order_id INT,
    product_variant_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_variant_id) REFERENCES product_variants(id)
);


-- RATINGS

CREATE TABLE ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    star INT CHECK (star BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP,
    user_id INT,
    product_id INT,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);


