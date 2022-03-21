/*
 Navicat Premium Data Transfer

 Source Server         : risktel
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 124.221.172.3:3306
 Source Schema         : lxfdb

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 21/03/2022 22:33:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for talkInfo
-- ----------------------------
DROP TABLE IF EXISTS `talkInfo`;
CREATE TABLE `talkInfo`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `talk_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通话id',
  `terminal_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通话信息',
  `phone_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机型号',
  `src_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '录音文件地址',
  `sound_state` int(0) NULL DEFAULT NULL COMMENT '声音状态1有声音0无声音',
  `proportion` double(255, 3) NULL DEFAULT NULL COMMENT '有声音占比',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
