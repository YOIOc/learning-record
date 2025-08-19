/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云-11.8截至
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 8.156.77.246:3306
 Source Schema         : hm-user

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 19/08/2025 16:44:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `province` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `town` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '县/区',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `street` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `contact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `is_default` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是默认 1默认 0否',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (59, 1, '北京', '北京', '朝阳区', '13900112222', '金燕龙办公楼', '李嘉诚', '0', NULL);
INSERT INTO `address` VALUES (60, 1, '北京', '北京', '朝阳区', '13700221122', '修正大厦', '李佳红', '0', NULL);
INSERT INTO `address` VALUES (61, 1, '上海', '上海', '浦东新区', '13301212233', '航头镇航头路', '李佳星', '1', NULL);
INSERT INTO `address` VALUES (63, 1, '广东', '佛山', '永春', '13301212233', '永春武馆', '李小龙', '0', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL,
  `status` int NULL DEFAULT 1 COMMENT '使用状态（1正常 2冻结）',
  `balance` int NULL DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Jack', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112224', '2017-08-19 20:50:21', '2017-08-19 20:50:21', 1, 1000000);
INSERT INTO `user` VALUES (2, 'Rose', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112223', '2017-08-19 21:00:23', '2017-08-19 21:00:23', 1, 1000000);
INSERT INTO `user` VALUES (3, 'Hope', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112222', '2017-08-19 22:37:44', '2017-08-19 22:37:44', 1, 1000000);
INSERT INTO `user` VALUES (4, 'Thomas', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '17701265258', '2017-08-19 23:44:45', '2017-08-19 23:44:45', 1, 1000000);

SET FOREIGN_KEY_CHECKS = 1;
