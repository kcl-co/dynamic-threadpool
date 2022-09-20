CREATE DATABASE IF NOT EXISTS dynamic_threadpool;

DROP TABLE IF EXISTS `dtp_server`;
CREATE TABLE IF NOT EXISTS `dtp_server`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `server_code`  VARCHAR(20)     NOT NULL DEFAULT '' COMMENT '服务代码',
    `server_name`  VARCHAR(45)     NOT NULL DEFAULT '' COMMENT '服务名称',
    `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_name` VARCHAR(45)     NOT NULL DEFAULT '' COMMENT '创建人名字',
    `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY uk_server_code (`server_code`)
) ENGINE = INNODB COMMENT = '动态线程池-服务实例';