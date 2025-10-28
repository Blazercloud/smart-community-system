-- =============================================
-- 东软智慧社区项目数据库初始化脚本
-- =============================================

-- 创建数据库（如果不存在）
DROP DATABASE IF EXISTS smart_community;
CREATE DATABASE IF NOT EXISTS smart_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE smart_community;

-- =============================================
-- 1. 用户中心模块
-- =============================================

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(11) UNIQUE NOT NULL COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE COMMENT '生日',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `address` VARCHAR(255) COMMENT '详细地址',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_phone (`phone`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 钱包表
CREATE TABLE IF NOT EXISTS `wallet` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '钱包ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `balance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-冻结，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- 账单表
CREATE TABLE IF NOT EXISTS `bill` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账单ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `bill_type` TINYINT NOT NULL COMMENT '账单类型：1-充值，2-消费，3-退款',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `balance_before` DECIMAL(10,2) COMMENT '变动前余额',
    `balance_after` DECIMAL(10,2) COMMENT '变动后余额',
    `description` VARCHAR(255) COMMENT '账单描述',
    `order_id` BIGINT COMMENT '关联订单ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单流水表';

-- =============================================
-- 2. 商城模块
-- =============================================

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `category` VARCHAR(50) COMMENT '商品分类',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `image` VARCHAR(255) COMMENT '商品图片',
    `description` TEXT COMMENT '商品描述',
    `store_id` BIGINT COMMENT '所属门店ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `sales_count` INT DEFAULT 0 COMMENT '销量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (`category`),
    INDEX idx_store_id (`store_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(32) UNIQUE NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT NOT NULL COMMENT '数量',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `payment_method` VARCHAR(20) COMMENT '支付方式',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
    `address` VARCHAR(255) COMMENT '收货地址',
    `receiver_name` VARCHAR(50) COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(11) COMMENT '收货人电话',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `pay_time` DATETIME COMMENT '支付时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    INDEX idx_order_no (`order_no`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 促销活动表
CREATE TABLE IF NOT EXISTS `promotion` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
    `description` TEXT COMMENT '活动描述',
    `discount_type` TINYINT COMMENT '优惠类型：1-满减，2-折扣',
    `discount_value` DECIMAL(10,2) COMMENT '优惠金额或折扣',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (`status`),
    INDEX idx_time (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- =============================================
-- 3. 社区模块
-- =============================================

-- 公告表
CREATE TABLE IF NOT EXISTS `notice` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `type` TINYINT DEFAULT 1 COMMENT '类型：1-社区公告，2-物业通知',
    `publisher_id` BIGINT COMMENT '发布人ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-已发布',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (`type`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 访客登记表
CREATE TABLE IF NOT EXISTS `visitor` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '访客ID',
    `user_id` BIGINT NOT NULL COMMENT '被访用户ID',
    `visitor_name` VARCHAR(50) NOT NULL COMMENT '访客姓名',
    `visitor_phone` VARCHAR(11) NOT NULL COMMENT '访客电话',
    `visit_time` DATETIME NOT NULL COMMENT '访问时间',
    `purpose` VARCHAR(255) COMMENT '访问目的',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已拒绝',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客登记表';

-- 报修表
CREATE TABLE IF NOT EXISTS `repair` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报修ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` TEXT NOT NULL COMMENT '描述',
    `location` VARCHAR(255) COMMENT '位置',
    `image` VARCHAR(255) COMMENT '图片',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完成，3-已取消',
    `worker_id` BIGINT COMMENT '维修工ID',
    `remark` VARCHAR(255) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

-- 投诉表
CREATE TABLE IF NOT EXISTS `complaint` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '投诉ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `type` TINYINT COMMENT '类型：1-物业服务，2-邻里纠纷，3-其他',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已解决，3-已关闭',
    `handler_id` BIGINT COMMENT '处理人ID',
    `reply` VARCHAR(255) COMMENT '回复',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉表';

-- 缴费表
CREATE TABLE IF NOT EXISTS `payment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '缴费ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `payment_type` TINYINT NOT NULL COMMENT '缴费类型：1-物业费，2-水电费，3-停车费',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `payment_time` DATETIME COMMENT '缴费时间',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未缴费，1-已缴费',
    `due_date` DATE COMMENT '缴费截止日期',
    `remark` VARCHAR(255) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费表';

-- =============================================
-- 4. 管理后台模块
-- =============================================

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `role_id` BIGINT COMMENT '角色ID',
    `phone` VARCHAR(11) COMMENT '电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (`username`),
    INDEX idx_role_id (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(50) UNIQUE NOT NULL COMMENT '角色编码',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 门店表
CREATE TABLE IF NOT EXISTS `store` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '门店ID',
    `name` VARCHAR(100) NOT NULL COMMENT '门店名称',
    `address` VARCHAR(255) COMMENT '地址',
    `phone` VARCHAR(11) COMMENT '电话',
    `manager_id` BIGINT COMMENT '管理员ID',
    `region_id` BIGINT COMMENT '所属区域ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-关闭，1-营业',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_manager_id (`manager_id`),
    INDEX idx_region_id (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店表';

-- 区域表
CREATE TABLE IF NOT EXISTS `region` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '区域ID',
    `name` VARCHAR(100) NOT NULL COMMENT '区域名称',
    `code` VARCHAR(50) COMMENT '区域编码',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID',
    `level` TINYINT COMMENT '级别：1-省，2-市，3-区',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域表';

-- 统计日志表
CREATE TABLE IF NOT EXISTS `stat_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `stat_type` VARCHAR(50) NOT NULL COMMENT '统计类型',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `stat_data` JSON COMMENT '统计数据（JSON格式）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_stat_type (`stat_type`),
    INDEX idx_stat_date (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统计日志表';

-- =============================================
-- 5. 系统模块
-- =============================================

-- 系统日志表
CREATE TABLE IF NOT EXISTS `log_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `module` VARCHAR(50) COMMENT '模块',
    `operation` VARCHAR(50) COMMENT '操作',
    `user_id` BIGINT COMMENT '用户ID',
    `user_type` TINYINT COMMENT '用户类型：1-普通用户，2-管理员',
    `request_url` VARCHAR(255) COMMENT '请求URL',
    `request_method` VARCHAR(10) COMMENT '请求方法',
    `request_params` TEXT COMMENT '请求参数',
    `response_data` TEXT COMMENT '响应数据',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `duration` INT COMMENT '耗时（毫秒）',
    `status` TINYINT COMMENT '状态：0-失败，1-成功',
    `error_msg` TEXT COMMENT '错误信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_module (`module`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 配置表
CREATE TABLE IF NOT EXISTS `config` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `description` VARCHAR(255) COMMENT '描述',
    `category` VARCHAR(50) COMMENT '分类',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (`config_key`),
    INDEX idx_category (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置表';

-- =============================================
-- 插入初始数据
-- =============================================

-- 插入默认管理员（密码: admin123，实际使用时应先加密）
INSERT INTO `admin` (`username`, `password`, `name`, `phone`, `email`) VALUES 
('admin', 'admin123', '系统管理员', '13800138000', 'admin@neusoft.com');

-- 插入默认角色
INSERT INTO `role` (`name`, `code`, `description`) VALUES 
('超级管理员', 'SUPER_ADMIN', '系统超级管理员'),
('普通管理员', 'ADMIN', '普通管理员'),
('运营人员', 'OPERATOR', '运营人员');

-- 插入测试用户（密码: user123）
INSERT INTO `user` (`phone`, `password`, `name`, `address`, `status`) VALUES 
('13800138001', 'user123', '张三', '智慧社区1号楼101', 1),
('13800138002', 'user123', '李四', '智慧社区2号楼202', 1),
('13800138003', 'user123', '王五', '智慧社区3号楼303', 1);

-- 为测试用户创建钱包
INSERT INTO `wallet` (`user_id`, `balance`) VALUES 
(1, 1000.00),
(2, 500.00),
(3, 200.00);

-- 插入默认商品
INSERT INTO `product` (`name`, `category`, `price`, `stock`, `description`) VALUES 
('新鲜苹果', '水果', 8.90, 100, '精选优质苹果'),
('牛奶', '饮品', 15.00, 50, '纯牛奶1L装'),
('大米', '主食', 45.00, 30, '优质大米5kg装'),
('纸巾', '日用品', 12.00, 200, '三层抽纸');

-- 插入默认公告
INSERT INTO `notice` (`title`, `content`, `type`, `status`) VALUES 
('欢迎入住智慧社区', '欢迎新业主入住，如有任何问题请联系物业', 1, 1),
('停车管理规定', '请遵守社区停车规定，违停车辆将被警告或罚款', 2, 1);

-- 插入默认配置
INSERT INTO `config` (`config_key`, `config_value`, `description`, `category`) VALUES 
('site_name', '东软智慧社区', '站点名称', 'system'),
('maintenance_mode', '0', '维护模式：0-关闭，1-开启', 'system'),
('order_expire_minutes', '30', '订单过期时间（分钟）', 'order');

