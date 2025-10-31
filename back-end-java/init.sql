-- ============================
-- 智慧社区平台数据库初始化脚本
-- Database: smart_community
-- Version: 2025-10-30
-- ============================

-- 若数据库已存在则删除
DROP DATABASE IF EXISTS smart_community;
CREATE DATABASE smart_community CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE smart_community;

-- ============================
-- 用户与管理员表
-- ============================

CREATE TABLE user (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(50) UNIQUE NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      phone VARCHAR(20),
                      email VARCHAR(100),
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(20) DEFAULT 'ADMIN',
                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO admin (username, password) VALUES ('admin', 'admin123');
INSERT INTO user (username, password, phone, email)
VALUES ('test_user', '123456', '13800001111', 'test@example.com');

-- ============================
-- 商城相关表
-- ============================

CREATE TABLE shop (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      owner_id INT NOT NULL,
                      description TEXT,
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (owner_id) REFERENCES user(id)
);

CREATE TABLE product (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         shop_id INT NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         stock INT DEFAULT 0,
                         description TEXT,
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (shop_id) REFERENCES shop(id)
);

CREATE TABLE `order` (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         products JSON NOT NULL,  -- [{"product_id":1,"quantity":2}]
                         total_price DECIMAL(10,2) NOT NULL,
                         status VARCHAR(20) DEFAULT 'pending',
                         payment_method VARCHAR(20) DEFAULT '微信支付',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 示例数据
INSERT INTO shop (name, owner_id, description)
VALUES ('社区便利店', 1, '主营日用品、零食等');

INSERT INTO product (shop_id, name, price, stock, description)
VALUES
    (1, '牙膏', 9.90, 100, '清新薄荷味'),
    (1, '洗衣液', 25.50, 50, '家庭装'),
    (1, '面包', 5.00, 80, '每日现烤');

INSERT INTO `order` (user_id, products, total_price, status, payment_method)
VALUES (1,
        '[{"product_id":1,"quantity":2},{"product_id":3,"quantity":1}]',
        24.8, 'completed', '支付宝');

-- ============================
-- 社区公告与维修表
-- ============================

CREATE TABLE community_notice (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  title VARCHAR(100) NOT NULL,
                                  content TEXT NOT NULL,
                                  publisher VARCHAR(50),
                                  publish_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE repair (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        worker_id INT,
                        description TEXT NOT NULL,
                        status VARCHAR(20) DEFAULT '未处理',
                        create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 示例数据
INSERT INTO community_notice (title, content, publisher)
VALUES ('停水通知', '因维修，自10月30日上午8点至下午5点停水，请提前储水。', '物业管理');

INSERT INTO repair (user_id, worker_id, description, status)
VALUES (1, 1, '厨房水管漏水', '处理中');

-- ============================
-- 维修师傅与车辆表
-- ============================

CREATE TABLE worker (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50) NOT NULL,
                        phone VARCHAR(20),
                        skill VARCHAR(100),
                        status VARCHAR(20) DEFAULT '空闲'
);

CREATE TABLE vehicle (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         plate_number VARCHAR(20) UNIQUE NOT NULL,
                         parking_spot VARCHAR(20),
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 示例数据
INSERT INTO worker (name, phone, skill, status)
VALUES ('张师傅', '13912345678', '水电维修', '忙碌');

INSERT INTO vehicle (user_id, plate_number, parking_spot)
VALUES (1, '粤A12345', 'B2-066');

-- ============================
-- 物业账单表
-- ============================

CREATE TABLE bill (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      user_id INT NOT NULL,
                      amount DECIMAL(10,2) NOT NULL,
                      due_date DATE,
                      status VARCHAR(20) DEFAULT '未缴费',
                      description TEXT,
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO bill (user_id, amount, due_date, status, description)
VALUES (1, 120.50, '2025-11-10', '未缴费', '10月份物业管理费');

-- ============================
-- 数据验证查询
-- ============================

SELECT '✅ 数据库 smart_community 初始化成功' AS status;
-- 为车位表添加车牌号字段
ALTER TABLE parking_space
    ADD COLUMN car_number VARCHAR(20) AFTER owner_id;

-- 为车位申请表添加车牌号字段
ALTER TABLE parking_application
    ADD COLUMN car_number VARCHAR(20) AFTER space_number;

-- 示例更新语句（给已有数据添加车牌号）
UPDATE parking_space SET car_number = '粤A12345' WHERE id = 1;
UPDATE parking_application SET car_number = '粤B67890' WHERE id = 1;
