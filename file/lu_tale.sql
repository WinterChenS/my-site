/*
 Navicat Premium Data Transfer

 Source Server         : mycould
 Source Server Type    : MariaDB
 Source Server Version : 50556
 Source Host           : 118.25.36.41
 Source Database       : lu_tale

 Target Server Type    : MariaDB
 Target Server Version : 50556
 File Encoding         : utf-8

 Date: 05/03/2018 17:01:52 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_attach`
-- ----------------------------
DROP TABLE IF EXISTS `t_attach`;
CREATE TABLE `t_attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(100) NOT NULL DEFAULT '',
  `ftype` varchar(50) DEFAULT '',
  `fkey` text NOT NULL,
  `authorId` int(10) DEFAULT NULL,
  `created` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;



-- ----------------------------
--  Table structure for `t_comments`
-- ----------------------------
DROP TABLE IF EXISTS `t_comments`;
CREATE TABLE `t_comments` (
  `coid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cid` int(10) unsigned DEFAULT '0',
  `created` int(10) unsigned DEFAULT '0',
  `author` varchar(200) DEFAULT NULL,
  `authorId` int(10) unsigned DEFAULT '0',
  `ownerId` int(10) unsigned DEFAULT '0',
  `mail` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `agent` varchar(200) DEFAULT NULL,
  `content` text,
  `type` varchar(16) DEFAULT 'comment',
  `status` varchar(16) DEFAULT 'approved',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`coid`),
  KEY `cid` (`cid`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_contents`
-- ----------------------------
DROP TABLE IF EXISTS `t_contents`;
CREATE TABLE `t_contents` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `titlePic` varchar(55) DEFAULT NULL,
  `slug` varchar(200) DEFAULT NULL,
  `created` int(10) unsigned DEFAULT '0',
  `modified` int(10) unsigned DEFAULT '0',
  `content` text COMMENT '内容文字',
  `authorId` int(10) unsigned DEFAULT '0',
  `type` varchar(16) DEFAULT 'post',
  `status` varchar(16) DEFAULT 'publish',
  `tags` varchar(200) DEFAULT NULL,
  `categories` varchar(200) DEFAULT NULL,
  `hits` int(10) unsigned DEFAULT '0',
  `commentsNum` int(10) unsigned DEFAULT '0',
  `allowComment` tinyint(1) DEFAULT '1',
  `allowPing` tinyint(1) DEFAULT '1',
  `allowFeed` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_logs`
-- ----------------------------
DROP TABLE IF EXISTS `t_logs`;
CREATE TABLE `t_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `action` varchar(100) DEFAULT NULL COMMENT '事件',
  `data` varchar(2000) DEFAULT NULL COMMENT '数据',
  `authorId` int(10) DEFAULT NULL COMMENT '作者编号',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `created` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_metas`
-- ----------------------------
DROP TABLE IF EXISTS `t_metas`;
CREATE TABLE `t_metas` (
  `mid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `slug` varchar(200) DEFAULT NULL,
  `type` varchar(32) NOT NULL DEFAULT '',
  `contentType` varchar(32) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `sort` int(10) unsigned DEFAULT '0',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`mid`),
  KEY `slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_options`
-- ----------------------------
DROP TABLE IF EXISTS `t_options`;
CREATE TABLE `t_options` (
  `name` varchar(32) NOT NULL DEFAULT '',
  `value` varchar(1000) DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `t_options`
-- ----------------------------
BEGIN;
INSERT INTO `t_options` VALUES ('baidu_site_verification', null, '百度网站验证码'), ('google_site_verification', null, 'google网站验证码'), ('site_description', null, '网站描述'), ('site_keywords', null, null), ('site_record', null, '备案号'), ('site_title', null, '网站标题'), ('social_csdn', null, 'csdn'), ('social_github', null, 'github'), ('social_jianshu', null, '简书地址'), ('social_resume', null, '简历地址'), ('social_twitter', null, 'twitter'), ('social_weibo', null, '微博地址'), ('social_zhihu', null, '知乎地址');
COMMIT;

-- ----------------------------
--  Table structure for `t_relationships`
-- ----------------------------
DROP TABLE IF EXISTS `t_relationships`;
CREATE TABLE `t_relationships` (
  `cid` int(10) unsigned NOT NULL,
  `mid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`cid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_users`
-- ----------------------------
DROP TABLE IF EXISTS `t_users`;
CREATE TABLE `t_users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `homeUrl` varchar(200) DEFAULT NULL,
  `screenName` varchar(32) DEFAULT NULL,
  `created` int(10) unsigned DEFAULT '0',
  `activated` int(10) unsigned DEFAULT '0',
  `logged` int(10) unsigned DEFAULT '0',
  `groupName` varchar(16) DEFAULT 'visitor',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `t_users` password 123456
-- ----------------------------
BEGIN;
INSERT INTO `t_users` VALUES ('1', 'admin', 'a66abb5684c45962d887564f08346e8d', '1034683568@qq.com', null, 'admin', '1490756162', '0', '0', 'visitor');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
