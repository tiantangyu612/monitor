/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50622
Source Host           : localhost:3306
Source Database       : monitor-manage

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2018-01-18 19:20:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) NOT NULL COMMENT '应用名称',
  `productId` int(11) NOT NULL COMMENT '所属产品id',
  `description` varchar(150) DEFAULT NULL COMMENT '应用描述',
  `owner` varchar(200) NOT NULL COMMENT '应用负责人',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表';

-- ----------------------------
-- Table structure for cluster
-- ----------------------------
DROP TABLE IF EXISTS `cluster`;
CREATE TABLE `cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) NOT NULL COMMENT '集群名称',
  `applicationId` int(11) NOT NULL COMMENT '所属应用',
  `description` varchar(150) DEFAULT NULL COMMENT '集群描述',
  `alarmUser` varchar(200) NOT NULL COMMENT '报警接收人',
  `alarmGroup` varchar(200) DEFAULT NULL COMMENT '集群报警接收组',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集群信息表';

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) NOT NULL COMMENT '产品名称',
  `owner` varchar(200) NOT NULL COMMENT '产品负责人',
  `description` varchar(150) DEFAULT NULL COMMENT '产品描述',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='产品表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `email` varchar(150) NOT NULL COMMENT '邮箱',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
