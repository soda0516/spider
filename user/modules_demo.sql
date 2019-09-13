/*
 Navicat Premium Data Transfer

 Source Server         : soda-server
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : 47.94.253.233:3306
 Source Schema         : modules_demo

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 06/09/2019 15:21:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_auth
-- ----------------------------
INSERT INTO `user_auth` VALUES (1, '测试改一');
INSERT INTO `user_auth` VALUES (2, '测试的名字');
INSERT INTO `user_auth` VALUES (3, '测试的名字');
INSERT INTO `user_auth` VALUES (4, '测试的名字');
INSERT INTO `user_auth` VALUES (5, '测试插入一个');
INSERT INTO `user_auth` VALUES (6, '测试插入一个');
INSERT INTO `user_auth` VALUES (7, '测试插入一个');
INSERT INTO `user_auth` VALUES (8, '测试插入一个');
INSERT INTO `user_auth` VALUES (9, '测试插入一个');
INSERT INTO `user_auth` VALUES (10, '测试插入一个');
INSERT INTO `user_auth` VALUES (11, '测试插入一个');
INSERT INTO `user_auth` VALUES (12, '测试插入一个');
INSERT INTO `user_auth` VALUES (13, '测试插入一个');
INSERT INTO `user_auth` VALUES (14, '测试插入一个');
INSERT INTO `user_auth` VALUES (15, '测试插入一个');
INSERT INTO `user_auth` VALUES (16, '测试插入一个');
INSERT INTO `user_auth` VALUES (17, '测试插入一个');
INSERT INTO `user_auth` VALUES (18, '测试插入一个');
INSERT INTO `user_auth` VALUES (19, '测试插入一个');
INSERT INTO `user_auth` VALUES (20, '测试插入一个');
INSERT INTO `user_auth` VALUES (21, '英语啊');
INSERT INTO `user_auth` VALUES (22, '测试');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKoqajbnsbcmpki28seidqrht6o`(`role_id`) USING BTREE,
  CONSTRAINT `FKoqajbnsbcmpki28seidqrht6o` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (7, '456123', 'Json再修改', 1);
INSERT INTO `user_info` VALUES (8, '123456', '添加一条数据', 1);
INSERT INTO `user_info` VALUES (9, '123456', '没有外键添加不了', 1);

-- ----------------------------
-- Table structure for user_miniapp
-- ----------------------------
DROP TABLE IF EXISTS `user_miniapp`;
CREATE TABLE `user_miniapp`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_note` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 'ROLE_ADMIN', '超级管理员');
INSERT INTO `user_role` VALUES (3, 'ROLE_TEST', '测试管理员');

-- ----------------------------
-- Table structure for user_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_role_auth`;
CREATE TABLE `user_role_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL,
  `auth_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_auth
-- ----------------------------
INSERT INTO `user_role_auth` VALUES (1, 1, 1);
INSERT INTO `user_role_auth` VALUES (2, 1, 2);
INSERT INTO `user_role_auth` VALUES (3, 1, 3);

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT,
  `jwt_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `expire_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_token
-- ----------------------------
INSERT INTO `user_token` VALUES (105, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWIiLCJhdWQiOiJhdWQiLCJhdXRob3JpdHkiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE1NjY4MTI2NjUsInVzZXJuYW1lIjoi5re75Yqg5LiA5p2h5pWw5o2uIn0.eZU6fKtiNExZetn-HF-yYcQPGHv5bnHOuOdrc0mpXAS5TRAkSvuLpgbw4z3gZ99jnIei5mgJy4u8RnyXcUwtOA', '添加一条数据', '2019-08-26 11:44:26');

SET FOREIGN_KEY_CHECKS = 1;
