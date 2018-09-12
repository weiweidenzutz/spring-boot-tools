/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50641
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50641
File Encoding         : 65001

Date: 2018-09-12 10:32:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1000', '1000元秒杀iphone6', '100', '2018-09-12 00:00:00', '2018-09-13 00:00:00', '2018-09-11 17:26:43');
INSERT INTO `seckill` VALUES ('1001', '800元秒杀ipad', '200', '2018-09-12 00:00:00', '2018-09-13 00:00:00', '2018-09-11 17:26:43');
INSERT INTO `seckill` VALUES ('1002', '6600元秒杀mac book pro', '300', '2018-09-12 00:00:00', '2018-09-13 00:00:00', '2018-09-11 17:26:43');
INSERT INTO `seckill` VALUES ('1003', '7000元秒杀iMac', '399', '2018-09-12 10:29:35', '2018-09-13 00:00:00', '2018-09-11 17:26:43');
