/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云-11.8截至
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 8.156.77.246:3306
 Source Schema         : hm-cart

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 19/08/2025 16:39:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车条目id ',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `item_id` bigint NOT NULL COMMENT 'sku商品id',
  `num` int NOT NULL DEFAULT 1 COMMENT '购买数量',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品标题',
  `spec` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品动态属性键值集',
  `price` int NOT NULL COMMENT '价格,单位：分',
  `image` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商品图片',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_user_item_id`(`user_id` ASC, `item_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单详情表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (7, 1, 100000006163, 1, '巴布豆(BOBDOG)柔薄悦动婴儿拉拉裤XXL码80片(15kg以上)', '{}', 67100, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t23998/350/2363990466/222391/a6e9581d/5b7cba5bN0c18fb4f.jpg!q70.jpg.webp', '2023-05-20 21:07:09', '2025-08-17 09:43:00');
INSERT INTO `cart` VALUES (12, 1, 40305713537, 1, '【官方正品 品牌直营】奥古狮登小白鞋女秋季透气2018新款百搭韩版休闲鞋运动鞋女鞋厚底板鞋女 浅蓝【厚底】 41', '{\"颜色\": \"金色\", \"尺码\": \"41\"}', 35400, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/14147/34/2890/275451/5c225484Ebd346404/e61513be4639ee2e.jpg!q70.jpg.webp', '2025-08-17 09:43:36', '2025-08-17 09:43:36');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
