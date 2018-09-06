/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.40-log : Database - mahjong
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mahjong` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `mahjong`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `open_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '微信openId',
  `nick_name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `head_icon` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `room_card` int(11) DEFAULT NULL COMMENT '房卡',
  `union_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '唯一编号',
  `province` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  `city` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '市',
  `sex` int(11) DEFAULT NULL COMMENT '性别0:男,1:女',
  `actual_card` int(11) DEFAULT NULL COMMENT '实际卡',
  `total_card` int(11) DEFAULT NULL COMMENT '总卡数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_login_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后登陆时间',
  `status` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '0:禁用,1：正常',
  `play_game` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '0:未玩过,1:玩过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `account` */

insert  into `account`(`id`,`uuid`,`open_id`,`nick_name`,`head_icon`,`room_card`,`union_id`,`province`,`city`,`sex`,`actual_card`,`total_card`,`create_time`,`last_login_time`,`status`,`play_game`) values (5,'100000','1111100000','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-05 14:10:08','0000-00-00 00:00:00','0','1'),(6,'100005','1111100002','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 10:44:27','0000-00-00 00:00:00','0','0'),(7,'100006','1111100021','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:08:34','0000-00-00 00:00:00','0','0'),(8,'100007','1111100022','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:09:04','0000-00-00 00:00:00','0','0'),(9,'100008','1111100024','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:10:14','0000-00-00 00:00:00','0','0'),(10,'100009','1111100031','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:28:38','0000-00-00 00:00:00','0','1'),(11,'100010','1111100032','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:28:44','0000-00-00 00:00:00','0','1'),(12,'100011','1111100033','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 11:34:34','0000-00-00 00:00:00','0','0'),(13,'100012','1111100030','测试','无',5,'11020310','浙江省','杭州市',0,5,5,'2018-09-06 15:42:58','0000-00-00 00:00:00','0','1');

/*Table structure for table `notice_table` */

DROP TABLE IF EXISTS `notice_table`;

CREATE TABLE `notice_table` (
  `id` bigint(20) NOT NULL COMMENT '主键编号',
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '内容',
  `title` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_persion` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='广告通知表';

/*Data for the table `notice_table` */

insert  into `notice_table`(`id`,`content`,`title`,`type`,`create_time`,`create_persion`) values (1,'欢迎加入深空麻将娱乐休闲游戏！',NULL,'1','2018-09-05 15:39:47',NULL);

/*Table structure for table `room_info` */

DROP TABLE IF EXISTS `room_info`;

CREATE TABLE `room_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键编号',
  `game_type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '1-转转麻将。2-划水麻将。3-长沙麻将',
  `ma` int(11) DEFAULT NULL COMMENT '抓码的个数',
  `zimo` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '/**\n                 *是否自摸胡，还是可以抢杠胡\n                 *0 可抢杠胡(默认)   1自摸胡\n                 */',
  `hong` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '是否红中当赖子',
  `seven_double` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '七小对',
  `room_id` int(11) DEFAULT NULL COMMENT '房间编号',
  `xia_yu` int(11) DEFAULT NULL COMMENT '下鱼(漂)(0--10)',
  `add_word_card` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '是否要字牌',
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '房间名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'createTime',
  `card_number` int(11) DEFAULT NULL COMMENT '消耗房卡数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='房间信息';

/*Data for the table `room_info` */

insert  into `room_info`(`id`,`game_type`,`ma`,`zimo`,`hong`,`seven_double`,`room_id`,`xia_yu`,`add_word_card`,`name`,`create_time`,`card_number`) values (5,'3',4,'1','0','0',205002,0,'0','','2018-09-06 10:47:27',1),(6,'3',4,'1','0','0',589094,0,'0','','2018-09-06 11:26:56',1),(7,'3',4,'1','0','0',105081,0,'0','','2018-09-06 11:28:22',1),(8,'3',4,'1','0','0',367835,0,'0','','2018-09-06 11:34:13',1),(9,'3',4,'1','0','0',166579,0,'0','','2018-09-06 15:36:22',1),(10,'3',4,'1','0','0',300900,0,'0','','2018-09-06 15:46:47',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
