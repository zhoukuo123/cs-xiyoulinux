create table cs_user_activity_comment
(
    id                  varchar(20) not null comment '主键'
        primary key,
    user_id             varchar(20) null comment '用户id',
    activity_id         varchar(20) not null comment '动态id',
    comment_content     text null comment '评论内容',
    comment_likes       int default 0 null comment '评论点赞数目',
    comment_create_time datetime null comment '评论创建时间'
) comment '用户评论表'  charset = utf8;

create table cs_user_likes
(
    id            varchar(20) not null comment '主键id'
        primary key,
    cs_comment_id varchar(20) not null comment '评论id',
    cs_user_id    varchar(20) not null comment '用户id',
    constraint cs_comment_likes_cs_comment_id_uindex
        unique (cs_comment_id, cs_user_id)
) comment '用户点赞评论记录'

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
) comment 'seata need' charset = utf8
