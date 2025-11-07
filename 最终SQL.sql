-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smart_community
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'ADMIN',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1001,'admin','admin123','ADMIN','2025-10-30 07:13:14',1),(1002,'admin2','admin321','东软社区','2025-10-31 08:12:55',1),(1003,'admin3','admin321','东软物业','2025-10-31 08:13:56',1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `fk_cart_product` (`product_id`),
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (48,1,56,3,'2025-11-01 00:30:00','2025-11-05 12:44:35'),(49,1,58,2,'2025-11-03 08:45:00','2025-11-05 12:44:35'),(50,2,57,4,'2025-11-02 02:15:00','2025-11-05 12:44:35'),(51,2,62,1,'2025-11-05 06:20:00','2025-11-05 12:44:35'),(52,7,65,2,'2025-11-04 01:50:00','2025-11-05 12:44:35'),(53,7,68,3,'2025-11-05 23:20:00','2025-11-05 12:44:35'),(54,9,61,1,'2025-11-01 05:30:00','2025-11-05 12:44:35'),(55,9,63,2,'2025-11-07 10:10:00','2025-11-05 12:44:35'),(56,14,70,5,'2025-11-03 03:20:00','2025-11-05 12:44:35'),(57,14,57,2,'2025-11-08 02:40:00','2025-11-05 12:44:35');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `complaint` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_handled` tinyint DEFAULT '0',
  `handle_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `complaint_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
INSERT INTO `complaint` VALUES (1,1,'楼道灯长期不亮',0,NULL,'2025-10-30 07:13:15'),(2,1,'小区环境需要改善',1,'物业已增加清洁频次','2025-11-05 02:09:17'),(3,1,'1号楼电梯经常故障，希望尽快维修',1,'已安排维修人员检修，预计3天内修复','2025-11-05 11:22:37'),(4,2,'小区垃圾桶清理不及时，异味严重',1,'已增加清理频次，每日两次','2025-11-05 11:22:37'),(5,7,'夜间广场舞噪音太大，影响休息',0,NULL,'2025-11-05 11:22:37'),(6,8,'3号楼楼道灯损坏，长期未修',1,'已更换灯泡，恢复照明','2025-11-05 11:22:37'),(7,9,'小区门口车辆乱停，堵塞通道',0,NULL,'2025-11-05 11:22:37'),(8,10,'自来水有异味，怀疑水质问题',1,'已联系自来水公司检测，水质合格','2025-11-05 11:22:37'),(9,7,'物业费偏高，服务不到位',0,NULL,'2025-11-05 11:22:37'),(10,8,'快递柜经常满，取件不便',1,'已新增一组快递柜','2025-11-05 11:22:37'),(11,9,'小区绿化杂草丛生，无人打理',0,NULL,'2025-11-05 11:22:37'),(12,10,'楼下商铺夜间经营噪音大',1,'已要求商铺调整营业时间，降低噪音','2025-11-05 11:22:37'),(13,11,'停车位不足，经常找不到车位',0,NULL,'2025-11-05 11:22:37'),(14,12,'楼道内有电动车充电，存在安全隐患',1,'已清理并张贴禁止充电通知','2025-11-05 11:22:37'),(15,13,'小区大门门禁失效，陌生人可随意进入',0,NULL,'2025-11-05 11:22:37'),(16,14,'垃圾清运车凌晨作业，噪音扰民',1,'已协调调整至早晨6点后作业','2025-11-05 11:22:37'),(17,15,'儿童游乐设施损坏，存在安全隐患',0,NULL,'2025-11-05 11:22:37');
/*!40000 ALTER TABLE `complaint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `type` tinyint DEFAULT '1' COMMENT '类型：1-社区公告，2-物业通知',
  `publisher_id` bigint DEFAULT NULL COMMENT '发布人ID',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-草稿，1-已发布，2-已过期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'小区电梯年度检修通知','尊敬的业主：为保障电梯安全运行，物业将于11月10日至11月12日期间对1号楼至3号楼电梯进行年度检修。期间请业主合理安排出行时间，由此带来的不便敬请谅解。',1,1001,0,'2025-10-31 16:04:24','2025-10-31 19:31:51'),(2,'停水通知','因市政管道维修，预计11月5日早上8:00至下午6:00期间，小区将暂停供水。请提前储水，感谢理解与支持！',1,1002,1,'2025-10-31 16:04:24','2025-10-31 19:32:06'),(3,'消防演练通知','为提高业主安全意识，本小区将于11月15日上午9:00在中心广场开展消防演练，请各位业主积极参与。',1,1003,0,'2025-10-31 16:04:24','2025-10-31 19:31:54'),(5,'地下车库清洗公告','物业将于11月8日（周六）上午8:00-12:00对地下车库进行清洗作业，请各位车主提前移动车辆。',2,1002,1,'2025-10-31 16:04:24','2025-10-31 16:04:24'),(6,'小区1号楼电梯年度检修通知','2025年11月10日-15日，对1号楼所有电梯进行年度安全检修，期间请绕行步行梯',1,1003,1,'2025-10-28 09:30:00','2025-10-31 20:38:11'),(7,'全小区消防演练通知','2025年11月20日上午10点，在小区中心广场开展消防演练，请业主配合疏散演练',1,1002,1,'2025-10-29 14:15:00','2025-10-31 19:44:23'),(9,'临时停水通知','因水管管网维护，2025年11月5日9:00-17:00全小区停水，请提前储备生活用水',1,1002,1,'2025-10-31 10:20:00','2025-10-31 10:20:00'),(10,'地下车库清洗公告','2025年11月12日全天清洗地下车库A区，请勿将车辆停放在A区车位，感谢配合',2,1003,1,'2025-10-27 16:00:00','2025-10-27 16:00:00'),(11,'垃圾分类宣传活动通知','为响应环保政策，小区将于2025年11月8日开展垃圾分类宣传活动，现场发放分类垃圾桶',1,1002,1,'2025-10-26 11:30:00','2025-10-31 19:46:48'),(12,'重阳节敬老活动邀请','10月17日重阳节，小区将为60岁以上业主提供免费体检服务，需提前到物业登记',2,1001,0,'2025-10-25 09:00:00','2025-11-02 15:27:43'),(13,'小区路灯检修通知','2025年11月3日晚22:00后，将对小区主干道路灯进行检修，可能临时熄灯，请注意安全',1,1003,0,'2025-10-24 15:40:00','2025-11-02 15:27:35'),(15,'天然气安全使用提醒','冬季来临，请注意天然气使用安全，定期检查管道是否泄漏，睡前关闭阀门',2,1002,1,'2025-10-22 13:20:00','2025-10-31 19:46:48'),(16,'2026年春节小区装饰方案','计划在小区大门、主干道布置春节装饰，预算5000元，需报领导审批',2,1001,0,'2025-10-31 11:00:00','2025-10-31 11:00:00'),(18,'电动汽车充电桩新增申请','拟在地下车库新增10个电动汽车充电桩，需协调电力公司施工',2,1001,0,'2025-10-29 09:50:00','2025-10-31 19:44:23'),(19,'装修施工管理规定修订','修订小区装修施工时间（工作日8:00-18:00），禁止夜间施工扰民',2,1003,0,'2025-10-28 16:20:00','2025-10-31 19:44:23'),(21,'小区健身器材维护计划','对小区健身器材进行全面检查和维护，更换损坏零件，确保使用安全',2,1002,0,'2025-10-26 15:10:00','2025-10-31 19:44:23'),(23,'小区宠物管理规定','规范小区宠物饲养，要求牵绳、清理粪便，禁止大型犬进入公共区域',2,1001,0,'2025-10-24 14:40:00','2025-10-31 19:46:48'),(24,'临时停车位规划方案','计划在小区南侧空地规划20个临时停车位，解决业主停车难问题',2,1003,0,'2025-10-23 09:30:00','2025-10-31 19:44:23'),(25,'物业员工业务培训通知','11月中旬组织物业员工进行服务礼仪和应急处理培训，提升服务水平',1,1003,0,'2025-10-22 16:00:00','2025-10-31 19:44:23'),(27,'物业通知ootd','我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆我哩个豆豆',2,1003,1,'2025-10-31 21:35:59','2025-10-31 21:35:59');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL COMMENT '订单ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,68,'猪肉包子2个',4.00,4,16.00),(2,1,69,'现磨豆浆',2.50,2,5.00),(3,1,60,'全麦面包',8.90,1,8.90),(4,2,70,'中性笔黑色0.5mm',2.00,5,10.00),(5,2,64,'笔记本',6.00,1,6.00),(6,3,56,'矿泉水550ml',2.00,2,4.00),(7,3,61,'洗衣液2kg',29.90,1,29.90),(8,3,69,'现磨豆浆',2.50,1,2.50),(9,4,68,'猪肉包子2个',4.00,2,8.00),(10,4,69,'现磨豆浆',2.50,2,5.00),(11,4,59,'可口可乐500ml',3.00,1,3.00),(12,5,57,'红烧牛肉面',4.50,3,13.50),(13,5,65,'圣女果500g',7.90,2,15.80),(14,5,63,'香蕉500g',5.50,1,5.50),(15,6,58,'抽纸3层10包',12.90,1,12.90),(16,6,62,'牙膏120g',9.90,1,9.90),(17,6,70,'中性笔黑色0.5mm',2.00,3,6.00),(18,7,66,'富士苹果1kg',12.80,1,12.80),(19,7,67,'上海青500g',3.50,2,7.00),(20,7,57,'红烧牛肉面',4.50,2,9.00),(21,8,56,'矿泉水550ml',2.00,4,8.00),(22,8,61,'洗衣液2kg',29.90,1,29.90),(23,8,59,'可口可乐500ml',3.00,2,6.00),(24,9,64,'笔记本',6.00,3,18.00),(25,9,70,'中性笔黑色0.5mm',2.00,4,8.00),(26,9,68,'猪肉包子2个',4.00,2,8.00),(27,10,57,'红烧牛肉面',4.50,2,9.00),(28,10,60,'全麦面包',8.90,1,8.90),(29,10,69,'现磨豆浆',2.50,3,7.50),(30,11,58,'抽纸3层10包',12.90,2,25.80),(31,11,62,'牙膏120g',9.90,1,9.90),(32,12,65,'圣女果500g',7.90,3,23.70),(33,12,66,'富士苹果1kg',12.80,1,12.80),(34,13,63,'香蕉500g',5.50,4,22.00),(35,13,67,'上海青500g',3.50,2,7.00),(36,14,56,'矿泉水550ml',2.00,3,6.00),(37,14,59,'可口可乐500ml',3.00,3,9.00),(38,15,64,'笔记本',6.00,2,12.00),(39,15,70,'中性笔黑色0.5mm',2.00,5,10.00),(40,16,68,'猪肉包子2个',4.00,3,12.00),(41,16,69,'现磨豆浆',2.50,4,10.00),(42,17,60,'全麦面包',8.90,2,17.80),(43,17,57,'红烧牛肉面',4.50,1,4.50),(44,18,61,'洗衣液2kg',29.90,1,29.90),(45,18,58,'抽纸3层10包',12.90,1,12.90),(46,19,62,'牙膏120g',9.90,2,19.80),(47,19,65,'圣女果500g',7.90,1,7.90),(48,20,66,'富士苹果1kg',12.80,2,25.60),(49,20,63,'香蕉500g',5.50,1,5.50);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `shop_id` int NOT NULL COMMENT '商家ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` varchar(20) DEFAULT 'pending' COMMENT '订单状态：pending-待付款, paid-已付款, shipped-已发货, completed-已完成, cancelled-已取消',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `shipping_address` text COMMENT '收货地址',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `shipping_time` datetime DEFAULT NULL COMMENT '发货时间',
  `tracking_no` varchar(50) DEFAULT NULL COMMENT '快递单号',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_order_shop` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORD20251020001',19,6,22.00,'completed','alipay','2025-10-20 07:30:00','小区4号楼2单元801室','用户15','13800138015','2025-10-20 07:45:00','SF1000001','2025-10-20 08:00:00','2025-10-19 23:20:00','2025-11-05 12:43:08'),(2,'ORD20251020002',15,1,18.90,'completed','wechat','2025-10-20 10:10:00','小区3号楼2单元601室','用户11','13800138011','2025-10-20 10:30:00','JD1000001','2025-10-20 11:00:00','2025-10-20 01:50:00','2025-11-05 12:43:08'),(3,'ORD20251020003',9,2,35.40,'pending',NULL,NULL,'小区2号楼1单元301室','用户05','13800138005',NULL,NULL,NULL,'2025-10-20 07:20:00','2025-11-05 12:43:08'),(4,'ORD20251022001',9,2,29.90,'completed','wechat','2025-10-22 14:00:00','小区2号楼1单元301室','用户05','13800138005','2025-10-22 14:30:00','YT1000001','2025-10-22 15:00:00','2025-10-22 05:50:00','2025-11-05 12:43:08'),(5,'ORD20251022002',12,1,8.00,'completed','alipay','2025-10-22 16:20:00','小区2号楼2单元402室','用户08','13800138008','2025-10-22 16:40:00','ZT1000001','2025-10-22 17:10:00','2025-10-22 08:10:00','2025-11-05 12:43:08'),(6,'ORD20251022003',7,3,26.20,'shipped','wechat','2025-10-22 18:10:00','小区3号楼203室','张三','15515551555','2025-10-22 18:30:00','SF1000002',NULL,'2025-10-22 09:50:00','2025-11-05 12:43:08'),(7,'ORD20251024001',10,9,12.00,'completed','alipay','2025-10-24 14:20:00','小区2号楼1单元302室','用户06','13800138006','2025-10-24 14:40:00','JD1000002','2025-10-24 15:10:00','2025-10-24 06:10:00','2025-11-05 12:43:08'),(8,'ORD20251024002',14,9,18.00,'completed','wechat','2025-10-24 15:30:00','小区3号楼1单元502室','用户10','13800138010','2025-10-24 15:50:00','YT1000002','2025-10-24 16:20:00','2025-10-24 07:20:00','2025-11-05 12:43:08'),(9,'ORD20251024003',1,1,12.90,'paid','alipay','2025-10-24 19:00:00','小区1号楼101室','张三','13800001111',NULL,NULL,NULL,'2025-10-24 10:50:00','2025-11-05 12:43:08'),(10,'ORD20251026001',11,3,15.80,'completed','wechat','2025-10-26 11:00:00','小区2号楼2单元401室','用户07','13800138007','2025-10-26 11:30:00','SF1000003','2025-10-26 12:00:00','2025-10-26 02:40:00','2025-11-05 12:43:08'),(11,'ORD20251026002',9,3,16.50,'completed','alipay','2025-10-26 19:00:00','小区2号楼1单元301室','用户05','13800138005','2025-10-26 19:30:00','JD1000003','2025-10-26 20:00:00','2025-10-26 10:40:00','2025-11-05 12:43:08'),(12,'ORD20251026003',12,2,29.90,'cancelled','wechat','2025-10-26 14:00:00','小区2号楼2单元402室','用户08','13800138008',NULL,NULL,NULL,'2025-10-26 05:30:00','2025-11-05 12:43:08'),(13,'ORD20251028001',2,1,13.50,'completed','alipay','2025-10-28 12:00:00','小区1号楼102室','李四','13800002222','2025-10-28 12:30:00','YT1000003','2025-10-28 13:00:00','2025-10-28 03:40:00','2025-11-05 12:43:08'),(14,'ORD20251028002',15,2,19.80,'completed','wechat','2025-10-28 15:00:00','小区3号楼2单元601室','用户11','13800138011','2025-10-28 15:30:00','ZT1000002','2025-10-28 16:00:00','2025-10-28 06:50:00','2025-11-05 12:43:08'),(15,'ORD20251028003',11,1,9.00,'paid','alipay','2025-10-28 19:30:00','小区2号楼2单元401室','用户07','13800138007',NULL,NULL,NULL,'2025-10-28 11:10:00','2025-11-05 12:43:08'),(16,'ORD20251030001',2,1,12.00,'completed','wechat','2025-10-30 16:30:00','小区1号楼102室','李四','13800002222','2025-10-30 17:00:00','SF1000004','2025-10-30 17:30:00','2025-10-30 08:15:00','2025-11-05 12:43:08'),(17,'ORD20251030002',14,1,9.00,'completed','alipay','2025-10-30 15:50:00','小区3号楼1单元502室','用户10','13800138010','2025-10-30 16:20:00','JD1000004','2025-10-30 16:50:00','2025-10-30 07:40:00','2025-11-05 12:43:08'),(18,'ORD20251030003',11,1,9.00,'completed','wechat','2025-10-30 19:30:00','小区2号楼2单元401室','用户07','13800138007','2025-10-30 20:00:00','YT1000004','2025-10-30 20:30:00','2025-10-30 11:10:00','2025-11-05 12:43:08'),(19,'ORD20251030004',7,3,12.80,'paid','alipay','2025-10-30 15:40:00','小区3号楼203室','张三','15515551555',NULL,NULL,NULL,'2025-10-30 07:30:00','2025-11-05 12:43:08'),(20,'ORD20251101001',1,1,4.00,'completed','alipay','2025-11-01 09:20:00','小区1号楼101室','张三','13800001111','2025-11-01 09:40:00','ZT1000003','2025-11-01 10:00:00','2025-11-01 01:10:00','2025-11-05 12:43:08'),(21,'ORD20251101002',2,2,9.90,'completed','wechat','2025-11-01 10:15:00','小区1号楼102室','李四','13800002222','2025-11-01 10:35:00','SF1000005','2025-11-01 11:00:00','2025-11-01 02:05:00','2025-11-05 12:43:08'),(22,'ORD20251101003',12,1,8.00,'completed','alipay','2025-11-01 11:00:00','小区2号楼2单元402室','用户08','13800138008','2025-11-01 11:20:00','JD1000005','2025-11-01 11:40:00','2025-11-01 02:50:00','2025-11-05 12:43:08'),(23,'ORD20251101004',15,1,6.00,'pending',NULL,NULL,'小区3号楼2单元601室','用户11','13800138011',NULL,NULL,NULL,'2025-11-01 03:10:00','2025-11-05 12:43:08'),(24,'ORD20251101005',9,2,29.90,'shipped','wechat','2025-11-01 16:20:00','小区2号楼1单元301室','用户05','13800138005','2025-11-01 16:40:00','YT1000005',NULL,'2025-11-01 08:10:00','2025-11-05 12:43:08'),(25,'ORD20251102001',1,1,12.90,'completed','wechat','2025-11-02 14:40:00','小区1号楼101室','张三','13800001111','2025-11-02 15:00:00','ZT1000004','2025-11-02 15:30:00','2025-11-02 06:30:00','2025-11-05 12:43:08'),(26,'ORD20251102002',9,3,7.00,'completed','alipay','2025-11-02 11:30:00','小区2号楼1单元301室','用户05','13800138005','2025-11-02 11:50:00','SF1000006','2025-11-02 12:20:00','2025-11-02 03:20:00','2025-11-05 12:43:08'),(27,'ORD20251102003',14,1,9.00,'paid','wechat','2025-11-02 19:30:00','小区3号楼1单元502室','用户10','13800138010',NULL,NULL,NULL,'2025-11-02 11:20:00','2025-11-05 12:43:08'),(28,'ORD20251102004',10,6,5.00,'completed','alipay','2025-11-02 08:10:00','小区2号楼1单元302室','用户06','13800138006','2025-11-02 08:30:00','JD1000006','2025-11-02 08:50:00','2025-11-02 00:00:00','2025-11-05 12:43:08'),(29,'ORD20251102005',7,6,8.00,'completed','wechat','2025-11-02 07:20:00','小区3号楼203室','张三','15515551555','2025-11-02 07:40:00','YT1000006','2025-11-02 08:00:00','2025-11-01 23:10:00','2025-11-05 12:43:08'),(30,'ORD20251103001',1,1,17.60,'completed','alipay','2025-11-03 19:30:00','小区1号楼101室','张三','13800001111','2025-11-03 20:00:00','ZT1000005','2025-11-03 20:30:00','2025-11-03 11:20:00','2025-11-05 12:43:08'),(31,'ORD20251103002',15,2,9.90,'completed','wechat','2025-11-03 11:10:00','小区3号楼2单元601室','用户11','13800138011','2025-11-03 11:30:00','SF1000007','2025-11-03 12:00:00','2025-11-03 03:00:00','2025-11-05 12:43:08'),(32,'ORD20251103003',11,3,11.00,'shipped','alipay','2025-11-03 14:30:00','小区2号楼2单元401室','用户07','13800138007','2025-11-03 15:00:00','JD1000007',NULL,'2025-11-03 06:20:00','2025-11-05 12:43:08'),(33,'ORD20251103004',12,6,5.00,'completed','wechat','2025-11-03 08:20:00','小区2号楼2单元402室','用户08','13800138008','2025-11-03 08:40:00','YT1000007','2025-11-03 09:00:00','2025-11-03 00:10:00','2025-11-05 12:43:08'),(34,'ORD20251103005',19,9,6.00,'pending',NULL,NULL,'小区4号楼2单元801室','用户15','13800138015',NULL,NULL,NULL,'2025-11-03 08:10:00','2025-11-05 12:43:08'),(35,'ORD20251104001',7,6,12.00,'completed','alipay','2025-11-04 07:20:00','小区3号楼203室','张三','15515551555','2025-11-04 07:40:00','ZT1000006','2025-11-04 08:00:00','2025-11-03 23:10:00','2025-11-05 12:43:08'),(36,'ORD20251104002',19,6,7.50,'completed','wechat','2025-11-04 07:40:00','小区4号楼2单元801室','用户15','13800138015','2025-11-04 08:00:00','SF1000008','2025-11-04 08:30:00','2025-11-03 23:30:00','2025-11-05 12:43:08'),(37,'ORD20251104003',10,9,10.00,'paid','alipay','2025-11-04 16:40:00','小区2号楼1单元302室','用户06','13800138006',NULL,NULL,NULL,'2025-11-04 08:30:00','2025-11-05 12:43:08'),(38,'ORD20251104004',14,1,9.00,'completed','wechat','2025-11-04 13:20:00','小区3号楼1单元502室','用户10','13800138010','2025-11-04 13:40:00','JD1000008','2025-11-04 14:00:00','2025-11-04 05:10:00','2025-11-05 12:43:08'),(39,'ORD20251104005',9,3,15.80,'cancelled','alipay','2025-11-04 10:30:00','小区2号楼1单元301室','用户05','13800138005',NULL,NULL,NULL,'2025-11-04 02:20:00','2025-11-05 12:43:08'),(40,'ORD20251105001',11,3,11.00,'completed','wechat','2025-11-05 14:30:00','小区2号楼2单元401室','用户07','13800138007','2025-11-05 15:00:00','YT1000008','2025-11-05 15:30:00','2025-11-05 06:20:00','2025-11-05 12:43:08'),(41,'ORD20251105002',10,6,5.00,'completed','alipay','2025-11-05 08:10:00','小区2号楼1单元302室','用户06','13800138006','2025-11-05 08:30:00','ZT1000007','2025-11-05 08:50:00','2025-11-05 00:00:00','2025-11-05 12:43:08'),(42,'ORD20251105003',14,1,9.00,'shipped','wechat','2025-11-05 13:20:00','小区3号楼1单元502室','用户10','13800138010','2025-11-05 13:40:00','SF1000009',NULL,'2025-11-05 05:10:00','2025-11-05 12:43:08'),(43,'ORD20251105004',19,9,6.00,'paid','alipay','2025-11-05 16:10:00','小区4号楼2单元801室','用户15','13800138015',NULL,NULL,NULL,'2025-11-05 08:00:00','2025-11-05 12:43:08'),(44,'ORD20251105005',2,3,20.70,'completed','wechat','2025-11-05 18:30:00','小区1号楼102室','李四','13800002222','2025-11-05 19:00:00','JD1000009','2025-11-05 19:30:00','2025-11-05 10:20:00','2025-11-05 12:43:08'),(45,'ORD20251106001',1,1,16.90,'completed','alipay','2025-11-06 09:20:00','小区1号楼101室','张三','13800001111','2025-11-06 09:40:00','YT1000009','2025-11-06 10:00:00','2025-11-06 01:10:00','2025-11-05 12:43:08'),(46,'ORD20251106002',1,6,13.00,'completed','wechat','2025-11-06 07:15:00','小区1号楼101室','张三','13800001111','2025-11-06 07:30:00','ZT1000008','2025-11-06 07:45:00','2025-11-05 23:05:00','2025-11-05 12:43:08'),(47,'ORD20251106003',2,2,49.40,'completed','alipay','2025-11-06 14:30:00','小区1号楼102室','李四','13800002222','2025-11-06 15:00:00','SF1000010','2025-11-06 15:30:00','2025-11-06 06:20:00','2025-11-05 12:43:08'),(48,'ORD20251106004',2,3,23.30,'shipped','wechat','2025-11-06 16:45:00','小区1号楼102室','李四','13800002222','2025-11-06 17:10:00','JD1000010',NULL,'2025-11-06 08:30:00','2025-11-05 12:43:08'),(49,'ORD20251106005',7,3,38.50,'completed','alipay','2025-11-06 10:15:00','小区3号楼203室','张三','15515551555','2025-11-06 10:40:00','YT1000010','2025-11-06 11:10:00','2025-11-06 02:05:00','2025-11-05 12:43:08'),(50,'ORD20251107001',7,6,9.00,'paid','wechat','2025-11-07 07:05:00','小区3号楼203室','张三','15515551555',NULL,NULL,NULL,'2025-11-06 23:00:00','2025-11-05 12:43:08'),(51,'ORD20251107002',9,2,51.20,'completed','alipay','2025-11-07 11:20:00','小区2号楼1单元301室','用户05','13800138005','2025-11-07 11:50:00','ZT1000009','2025-11-07 12:20:00','2025-11-07 03:10:00','2025-11-05 12:43:08'),(52,'ORD20251107003',9,3,14.50,'cancelled','wechat','2025-11-07 13:30:00','小区2号楼1单元301室','用户05','13800138005',NULL,NULL,NULL,'2025-11-07 05:20:00','2025-11-05 12:43:08'),(53,'ORD20251107004',14,9,16.00,'completed','alipay','2025-11-07 15:40:00','小区3号楼1单元502室','用户10','13800138010','2025-11-07 16:10:00','SF1000011','2025-11-07 16:40:00','2025-11-07 07:30:00','2025-11-05 12:43:08'),(54,'ORD20251107005',14,1,7.00,'pending',NULL,NULL,'小区3号楼1单元502室','用户10','13800138010',NULL,NULL,NULL,'2025-11-07 08:20:00','2025-11-05 12:43:08'),(55,'ORD20251108001',19,6,22.00,'completed','wechat','2025-11-08 06:50:00','小区4号楼2单元801室','用户15','13800138015','2025-11-08 07:10:00','JD1000011','2025-11-08 07:30:00','2025-11-07 22:40:00','2025-11-05 12:43:08'),(56,'ORD20251108002',19,9,10.00,'paid','alipay','2025-11-08 14:15:00','小区4号楼2单元801室','用户15','13800138015','2025-11-08 14:45:00',NULL,NULL,'2025-11-08 06:10:00','2025-11-05 12:43:08'),(57,'ORD20251108003',12,6,5.00,'completed','wechat','2025-11-08 08:20:00','小区2号楼2单元402室','用户08','13800138008','2025-11-08 08:40:00','YT1000011','2025-11-08 09:00:00','2025-11-08 00:15:00','2025-11-05 12:43:08'),(58,'ORD20251108004',15,6,14.50,'completed','alipay','2025-11-08 07:25:00','小区3号楼2单元601室','用户11','13800138011','2025-11-08 07:40:00','ZT1000010','2025-11-08 08:00:00','2025-11-07 23:15:00','2025-11-05 12:43:08');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_application`
--

DROP TABLE IF EXISTS `parking_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_application` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户ID',
  `space_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车位号',
  `car_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车牌号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0-待审批，1-已同意，2-已退回',
  `apply_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `parking_application_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_application`
--

LOCK TABLES `parking_application` WRITE;
/*!40000 ALTER TABLE `parking_application` DISABLE KEYS */;
INSERT INTO `parking_application` VALUES (8,1,'A-003','晋A11223','1','2025-11-04 03:13:24'),(9,7,'A-001','京B12345','1','2025-11-04 07:06:26'),(10,7,'A-004','晋A11221','0','2025-11-04 08:30:45'),(11,7,'A-004','晋A11223','0','2025-11-04 08:30:51'),(12,7,'A-004','晋A11223','0','2025-11-04 08:31:37'),(13,7,'A-004','晋A11223','0','2025-11-04 08:32:37'),(14,7,'A-004','晋A11223','0','2025-11-04 08:33:37');
/*!40000 ALTER TABLE `parking_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_space`
--

DROP TABLE IF EXISTS `parking_space`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_space` (
  `id` int NOT NULL AUTO_INCREMENT,
  `space_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '空闲' COMMENT '0-空闲，1-已占用',
  `owner_id` int DEFAULT NULL,
  `car_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `space_number` (`space_number`) USING BTREE,
  KEY `owner_id` (`owner_id`) USING BTREE,
  CONSTRAINT `parking_space_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_space`
--

LOCK TABLES `parking_space` WRITE;
/*!40000 ALTER TABLE `parking_space` DISABLE KEYS */;
INSERT INTO `parking_space` VALUES (1,'A-001','已占用',7,'京B12345'),(2,'A-002','已占用',1,'粤A12345'),(3,'A-003','已占用',1,'晋A11223'),(5,'A-004','空闲',NULL,NULL),(6,'A-005','空闲',NULL,NULL),(7,'A-006','空闲',NULL,NULL),(8,'A-007','空闲',NULL,NULL),(9,'A-008','空闲',NULL,NULL),(10,'A-009','空闲',NULL,NULL),(11,'A-010','空闲',NULL,NULL),(12,'A-011','空闲',NULL,NULL),(13,'A-012','空闲',NULL,NULL),(14,'A-013','空闲',NULL,NULL),(15,'A-014','空闲',NULL,NULL),(16,'A-015','空闲',NULL,NULL),(17,'A-016','已占用',1,'晋A11224'),(18,'A-017','空闲',NULL,NULL);
/*!40000 ALTER TABLE `parking_space` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shop_id` int NOT NULL COMMENT '商家ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `category` varchar(50) DEFAULT '其他' COMMENT '商品分类',
  `images` text COMMENT '商品图片',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (56,1,'矿泉水550ml','天然矿泉水，口感清爽',2.00,200,'饮品','/images/water.jpg',1,'2025-11-05 12:31:14','2025-11-06 06:38:03'),(57,1,'红烧牛肉面','经典口味方便面，非油炸面饼',4.50,150,'食品','/images/noodle1.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(58,1,'抽纸3层10包','原生木浆，柔韧亲肤',12.90,80,'日用品','/images/tissue.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(59,1,'可口可乐500ml','碳酸饮料，冰镇更佳',3.00,180,'饮品','/images/coke.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(60,1,'全麦面包','无添加蔗糖，健康早餐',8.80,60,'食品','/images/bread.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(61,2,'洗衣液2kg','深层洁净，低泡易漂',29.90,50,'日用品','/images/laundry.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(62,2,'牙膏120g','清新口气，预防蛀牙',9.90,100,'日用品','/images/toothpaste.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(63,2,'香蕉500g','新鲜进口香蕉，香甜软糯',5.50,30,'生鲜','/images/banana.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(64,2,'笔记本','A5规格，80页，缝线装订',6.00,70,'文具','/images/notebook.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(65,3,'圣女果500g','新鲜圣女果，酸甜多汁',7.90,40,'蔬菜','/images/cherry.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(66,3,'富士苹果1kg','脆甜可口，富含维生素',12.80,25,'水果','/images/apple.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(67,3,'上海青500g','绿叶蔬菜，清炒佳品',3.50,50,'蔬菜','/images/vegetable1.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(68,6,'猪肉包子2个','新鲜猪肉馅，皮薄馅大',4.00,100,'早餐','/images/bun.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(69,6,'现磨豆浆','无糖豆浆，营养健康',2.50,80,'饮品','/images/soybean.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(70,9,'中性笔黑色0.5mm','顺滑书写，防水油墨',2.00,200,'文具','/images/pen.jpg',1,'2025-11-05 12:31:14','2025-11-05 12:31:14'),(71,1,'111','1233211234567',1000.00,200,'其他',NULL,1,'2025-11-06 07:24:07','2025-11-06 07:24:07');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_payment`
--

DROP TABLE IF EXISTS `property_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_payment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '缴费订单ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `type` varchar(20) NOT NULL COMMENT '费用类型：property/parking/other',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '支付状态：pending/paid/refunded',
  `pay_method` varchar(20) DEFAULT NULL COMMENT '支付方式：wallet/alipay/wechat',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_property_payment_user` (`user_id`),
  CONSTRAINT `fk_property_payment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物业缴费订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_payment`
--

LOCK TABLES `property_payment` WRITE;
/*!40000 ALTER TABLE `property_payment` DISABLE KEYS */;
INSERT INTO `property_payment` VALUES (1,1,200.00,'property','pending','','2025年物业费待缴','2025-11-04 10:54:36','2025-11-04 11:27:27'),(2,2,300.00,'property','paid','admin','手动标记缴费','2025-11-04 10:55:07','2025-11-05 11:36:04'),(3,1,300.00,'property','paid','wechat','2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:35:45'),(7,2,250.00,'property','paid','alipay','2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(8,7,300.00,'property','pending',NULL,'2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(9,8,200.00,'parking','refunded','wechat','2023Q3停车费（退费）','2025-11-05 19:21:34','2025-11-05 19:21:34'),(10,9,350.00,'property','paid','alipay','2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(11,10,150.00,'other','paid','wechat','垃圾处理费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(12,7,280.00,'property','pending',NULL,'2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(13,8,400.00,'parking','paid','alipay','2023Q4停车费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(14,9,300.00,'property','refunded','wechat','物业费（退费）','2025-11-05 19:21:34','2025-11-05 19:21:34'),(15,10,260.00,'property','paid','alipay','2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(16,11,320.00,'parking','paid','wechat','2023Q4停车费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(17,12,290.00,'property','pending',NULL,'2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(18,13,100.00,'other','paid','alipay','公共设施维护费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(19,14,330.00,'property','paid','wechat','2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34'),(20,15,270.00,'property','pending',NULL,'2023Q4物业费','2025-11-05 19:21:34','2025-11-05 19:21:34');
/*!40000 ALTER TABLE `property_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair`
--

DROP TABLE IF EXISTS `repair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `repair_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报修类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报修标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细描述',
  `img_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片URL',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系电话',
  `expected_time` datetime DEFAULT NULL COMMENT '期望维修时间',
  `status` tinyint DEFAULT '0' COMMENT '报修状态：0-未审核，1-已审核，2-已处理',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `assigned_worker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指派维修人员',
  `handle_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '处理详情',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair`
--

LOCK TABLES `repair` WRITE;
/*!40000 ALTER TABLE `repair` DISABLE KEYS */;
INSERT INTO `repair` VALUES (1,1,'plumbing','厨房水龙头漏水','厨房水槽下方接头漏水严重，请尽快处理。','http://127.0.0.1:9001/community-img/faucet1.png','13800001111','2025-11-10 09:00:00',0,'2025-11-06 11:32:17','2025-11-06 11:32:17',NULL,NULL),(2,2,'electrical','客厅灯不亮','可能是线路老化，插座正常但灯泡不亮。','http://127.0.0.1:9001/community-img/light1.png','13800002222','2025-11-11 15:00:00',1,'2025-11-06 11:32:17','2025-11-06 11:32:17','张师傅','已检查，线路老化，更换线材完成'),(3,7,'furniture','门把手松动','卧室门把手松动，有掉落风险。','http://127.0.0.1:9001/community-img/doorhandle.png','13800003333','2025-11-09 13:00:00',2,'2025-11-06 11:32:17','2025-11-06 11:32:17','李工','已紧固并添加润滑油'),(4,8,'network','家中WiFi信号弱','最近几天信号经常中断，请物业检查信号放大器。','http://127.0.0.1:9001/community-img/wifi.png','13800004444','2025-11-10 10:00:00',0,'2025-11-06 11:32:17','2025-11-06 11:32:17',NULL,NULL),(5,9,'public_facility','电梯按钮失灵','3号楼电梯“5层”按钮无法使用。','http://127.0.0.1:9001/community-img/elevator.png','13800005555','2025-11-09 18:00:00',1,'2025-11-06 11:32:17','2025-11-06 11:32:17','周维修','更换按钮模块，电梯恢复正常'),(6,10,'security','门禁系统无反应','刷卡无效，进出困难。','http://127.0.0.1:9001/community-img/doorlock.png','13800006666','2025-11-12 08:30:00',0,'2025-11-06 11:32:17','2025-11-06 11:32:17',NULL,NULL),(7,1,'painting','墙面发霉','卫生间墙壁出现黑斑，希望重新粉刷。','http://127.0.0.1:9001/community-img/mold.png','13800007777','2025-11-13 10:00:00',2,'2025-11-06 11:32:17','2025-11-06 11:32:17','刘工','已刮除霉斑并重新涂漆'),(8,2,'air_conditioning','空调漏水','卧室空调滴水，影响休息。','http://127.0.0.1:9001/community-img/ac.png','13800008888','2025-11-14 15:00:00',1,'2025-11-06 11:32:17','2025-11-06 11:32:17','陈师傅','排水管堵塞，已清理完毕'),(9,7,'public_facility','垃圾桶破损','小区东门垃圾桶底部破洞。','http://127.0.0.1:9001/community-img/bin.png','13800009999','2025-11-10 16:00:00',2,'2025-11-06 11:32:17','2025-11-06 11:32:17','黄维修','更换新垃圾桶'),(10,8,'lighting','走廊灯闪烁','夜间闪烁严重，影响居民休息。','http://127.0.0.1:9001/community-img/hallway.png','13800001234','2025-11-15 19:00:00',1,'2025-11-06 11:32:17','2025-11-06 11:32:17','王电工','更换节能灯，运行正常'),(11,9,'water_supply','公共水龙头坏了','园区中间喷泉边的水龙头无法关闭。','http://127.0.0.1:9001/community-img/tap.png','13800002345','2025-11-16 09:00:00',0,'2025-11-06 11:32:17','2025-11-06 11:32:17',NULL,NULL),(12,10,'network','光纤接口松动','客厅墙角光纤接口松动，网速不稳定。','http://127.0.0.1:9001/community-img/fiber.png','13800003456','2025-11-17 11:00:00',2,'2025-11-06 11:32:17','2025-11-06 11:32:17','赵工','已固定接口并测试通过'),(13,11,'security','地下车库摄像头损坏','监控黑屏，安全隐患大。','http://127.0.0.1:9001/community-img/camera.png','13800004567','2025-11-18 08:30:00',1,'2025-11-06 11:32:17','2025-11-06 11:32:17','马维修','更换摄像头模块'),(14,12,'plumbing','浴室排水不畅','每次洗澡排水速度非常慢。','http://127.0.0.1:9001/community-img/drain.png','13800005678','2025-11-19 14:30:00',0,'2025-11-06 11:32:17','2025-11-06 11:32:17',NULL,NULL),(15,1,'electrical','厨房插座烧焦','使用电饭煲时冒烟，插座焦黑。','http://127.0.0.1:9001/community-img/socket.png','13800006789','2025-11-20 10:00:00',2,'2025-11-06 11:32:17','2025-11-06 11:32:17','刘电工','更换插座，检查电线安全');
/*!40000 ALTER TABLE `repair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `owner_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺地址',
  `business_hours` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '营业时间',
  `logo` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺logo',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-停业，1-营业',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `shop_unique_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'社区便利店','王老板','shop00','123456','13911112222','社区便民超市，提供日常生活用品和食品','2025-10-30 07:13:14','小区商业街1号','08:00-22:00',NULL,1),(2,'便民超市','张三','shop01','123456','13900139001','日常用品销售','2025-11-05 11:06:29','小区1号楼底商','08:00-22:00','/logo/shop01.jpg',1),(3,'生鲜果蔬店','李四','shop02','123456','13900139002','新鲜水果、蔬菜','2025-11-05 11:06:29','小区2号楼底商','07:00-21:00','/logo/shop02.jpg',1),(4,'五金建材店','王五','shop03','123456','13900139003','五金工具、建材','2025-11-05 11:06:29','小区3号楼底商','09:00-18:00','/logo/shop03.jpg',0),(5,'美容美发店','赵六','shop04','123456','13900139004','美容、理发服务','2025-11-05 11:06:29','小区4号楼底商','10:00-21:00','/logo/shop04.jpg',1),(6,'早餐店','钱七','shop05','123456','13900139005','包子、豆浆、油条','2025-11-05 11:06:29','小区5号楼底商','06:00-10:30','/logo/shop05.jpg',1),(7,'药店','孙八','shop06','123456','13900139006','处方药、非处方药','2025-11-05 11:06:29','小区6号楼底商','08:30-21:30','/logo/shop06.jpg',1),(8,'干洗店','周九','shop07','123456','13900139007','衣物干洗、熨烫','2025-11-05 11:06:29','小区7号楼底商','09:00-19:00','/logo/shop07.jpg',1),(9,'文具店','吴十','shop08','123456','13900139008','文具、办公用品','2025-11-05 11:06:29','小区8号楼底商','09:00-20:00','/logo/shop08.jpg',1),(10,'宠物店','郑一','shop09','123456','13900139009','宠物用品、美容','2025-11-05 11:06:29','小区9号楼底商','10:00-20:00','/logo/shop09.jpg',0),(11,'蛋糕店','王二','shop10','123456','13900139010','生日蛋糕、甜点','2025-11-05 11:06:29','小区10号楼底商','09:00-21:00','/logo/shop10.jpg',1),(12,'花店','刘一','shop11','123456','13900139011','鲜花、绿植','2025-11-05 11:06:29','小区11号楼底商','09:30-20:30','/logo/shop11.jpg',1),(13,'书店','陈二','shop12','123456','13900139012','各类图书','2025-11-05 11:06:29','小区12号楼底商','10:00-21:00','/logo/shop12.jpg',1),(14,'家电维修','杨三','shop13','123456','13900139013','家电维修服务','2025-11-05 11:06:29','小区13号楼底商','09:00-18:00','/logo/shop13.jpg',1),(15,'水果店','黄四','shop14','123456','13900139014','进口水果','2025-11-05 11:06:29','小区14号楼底商','08:00-21:00','/logo/shop14.jpg',1),(16,'面包店','朱五','shop15','123456','13900139015','面包、糕点','2025-11-05 11:06:29','小区15号楼底商','07:00-20:00','/logo/shop15.jpg',1);
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `house_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zhangsan','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800001111','zhangsan@example.com','1-101','两室一厅','2025-10-30 07:13:14',1),(2,'lisi','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800002222','lisi@example.com','1-102','三室两厅','2025-10-30 07:13:14',1),(7,'张三','$2a$10$xivFqIKQxYfIfhJZIMfeNeGXiuEEfhoKAeWdTNA8VjCnzB2w0IP8O','15515551555',NULL,NULL,NULL,'2025-11-04 07:05:07',1),(8,'user04','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138004','user04@test.com','1-2-202','一室一厅','2025-11-05 11:04:49',0),(9,'user05','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138005','user05@test.com','2-1-301','两室两厅','2025-11-05 11:04:49',1),(10,'user06','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138006','user06@test.com','2-1-302','三室一厅','2025-11-05 11:04:49',1),(11,'user07','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138007','user07@test.com','2-2-401','两室一厅','2025-11-05 11:04:49',1),(12,'user08','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138008','user08@test.com','2-2-402','一室一厅','2025-11-05 11:04:49',1),(13,'user09','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138009','user09@test.com','3-1-501','三室两厅','2025-11-05 11:04:49',0),(14,'user10','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138010','user10@test.com','3-1-502','两室一厅','2025-11-05 11:04:49',1),(15,'user11','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138011','user11@test.com','3-2-601','三室一厅','2025-11-05 11:04:49',1),(16,'user12','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138012','user12@test.com','3-2-602','两室两厅','2025-11-05 11:04:49',1),(17,'user13','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138013','user13@test.com','4-1-701','一室一厅','2025-11-05 11:04:49',1),(18,'user14','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138014','user14@test.com','4-1-702','三室一厅','2025-11-05 11:04:49',0),(19,'user15','$2a$10$hHFdso5Hoq9kavTyrRua/.dN7VTJv/PJEwPjdguPW8cpkpnTyvlKy','13800138015','user15@test.com','4-2-801','两室一厅','2025-11-05 11:04:49',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `visitor_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `purpose` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `visit_time` datetime DEFAULT NULL,
  `related_user` int DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `related_user` (`related_user`) USING BTREE,
  CONSTRAINT `visitor_ibfk_1` FOREIGN KEY (`related_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
INSERT INTO `visitor` VALUES (1,'王五','亲友','探望','2025-10-30 10:00:00',1,1),(2,'李四','外卖','七号楼外卖\n','2025-11-05 18:43:22',2,0),(48,'张三','亲友','拜访家人','2023-10-01 09:00:00',1,1),(49,'李四','维修人员','家电维修','2023-10-01 10:30:00',2,1),(50,'王五','快递员','送快递','2023-10-01 11:00:00',12,1),(51,'赵六','亲友','聚餐','2023-10-01 18:00:00',9,0),(52,'钱七','装修人员','房屋装修','2023-10-02 08:30:00',9,1),(53,'孙八','亲友','看望老人','2023-10-02 14:00:00',10,1),(54,'周九','外卖员','送外卖','2023-10-02 12:00:00',7,1),(55,'吴十','维修人员','管道维修','2023-10-03 09:00:00',8,1),(56,'郑一','亲友','借住','2023-10-03 16:00:00',9,0),(57,'王二','快递员','取件','2023-10-03 10:00:00',10,1),(58,'刘一','装修人员','安装家具','2023-10-04 09:30:00',11,1),(59,'陈二','亲友','周末拜访','2023-10-04 15:00:00',12,1),(60,'杨三','外卖员','送外卖','2023-10-04 12:30:00',13,1),(61,'黄四','维修人员','空调维修','2023-10-05 10:00:00',14,1),(62,'朱五','亲友','过节拜访','2023-10-05 16:00:00',15,1);
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-07  9:52:15
