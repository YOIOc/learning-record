/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云-11.8截至
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 8.156.77.246:3306
 Source Schema         : hm-pay

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 19/08/2025 16:43:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `biz_order_no` bigint NOT NULL COMMENT '业务订单号',
  `pay_order_no` bigint NOT NULL DEFAULT 0 COMMENT '支付单号',
  `biz_user_id` bigint NOT NULL COMMENT '支付用户id',
  `pay_channel_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
  `amount` int NOT NULL COMMENT '支付金额，单位分',
  `pay_type` tinyint NOT NULL DEFAULT 5 COMMENT '支付类型，1：h5,2:小程序，3：公众号，4：扫码，5：余额支付',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功',
  `expand_json` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '拓展字段，用于传递不同渠道单独处理的字段',
  `result_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方返回业务码',
  `result_msg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方返回提示信息',
  `pay_success_time` datetime NULL DEFAULT NULL COMMENT '支付成功时间',
  `pay_over_time` datetime NOT NULL COMMENT '支付超时时间',
  `qr_code_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付二维码链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT 0 COMMENT '更新人',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `biz_order_no`(`biz_order_no` ASC) USING BTREE,
  UNIQUE INDEX `pay_order_no`(`pay_order_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1659160218174607364 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES (1658455441987891201, 1658453559437434882, 1658455441975308289, 1, 'balance', 55400, 5, 3, '', '', '', '2023-05-16 21:14:57', '2023-05-16 22:52:45', NULL, '2023-05-16 20:52:44', '2023-05-18 19:38:03', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1659160218174607363, 1659160216593354754, 1659160218174607362, 1, 'balance', 156000, 5, 3, '', '', '', '2023-05-18 19:34:48', '2023-05-18 21:33:16', NULL, '2023-05-18 19:33:16', '2023-05-18 19:37:54', 0, 0, b'0');

SET FOREIGN_KEY_CHECKS = 1;
