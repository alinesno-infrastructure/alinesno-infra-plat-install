-- 创建数据库 leaf
CREATE DATABASE leaf;

-- 创建表 leaf_alloc
CREATE TABLE `leaf_alloc` (
    `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
    `delete_manager` BIGINT COMMENT '删除的人',
    `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
    `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
    `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
    `field_id` VARCHAR(255) COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
    `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
    `id` BIGINT COMMENT '唯一ID号',
    `field_prop` VARCHAR(255) COMMENT '字段属性',
    `add_time` DATETIME COMMENT '添加时间',
    `delete_time` DATETIME COMMENT '删除时间',
    `has_status` INT COMMENT '状态(0启用|1禁用)',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
    `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',

    `biz_tag` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '业务标签',
    `max_id` BIGINT NOT NULL DEFAULT '1' COMMENT '最大ID',
    `step` INT NOT NULL COMMENT '步长',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Leaf Alloc';

-- 插入数据
INSERT INTO leaf_alloc(biz_tag, max_id, step, description) VALUES ('leaf-segment-test', 1, 2000, 'Test leaf Segment Mode Get Id');
