create table cs_activity_comment_file
(
    id               varchar(20) not null comment '主键id'
        primary key,
    comment_id       varchar(20) null comment '评论id',
    comment_file_url varchar(200) null comment '评论内容包含的文件url,以逗号分隔',
    activity_id      varchar(20) null comment '动态id'
) comment '动态评论包含的图片' charset = utf8;

create table cs_activity_file
(
    id                varchar(20) not null comment '主键id'
        primary key,
    activity_id       varchar(20) null comment '动态id',
    activity_file_url varchar(200) null comment '动态内容包含的文件url,以逗号分隔'
) comment '动态内容包含的图片' charset = utf8;

create table cs_user_file
(
    id           varchar(20) null comment '主键',
    user_id      varchar(20) null comment '用户id',
    user_pic_url varchar(200) null comment '用户图片url'
) comment '用户图片表' charset = utf8;

create table undo_log
(
    id            bigint auto_increment
        primary key,
    branch_id     bigint       not null,
    xid           varchar(100) not null,
    context       varchar(128) not null,
    rollback_info longblob     not null,
    log_status    int          not null,
    log_created   datetime     not null,
    log_modified  datetime     not null,
    ext           varchar(100) null,
    constraint ux_undo_log
        unique (xid, branch_id)
) comment 'seata need' charset = utf8;