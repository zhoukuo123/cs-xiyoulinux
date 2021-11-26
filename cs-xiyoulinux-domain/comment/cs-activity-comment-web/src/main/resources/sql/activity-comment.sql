create table cs_user_activity_comment
(
    id                  varchar(20)   not null comment '主键'
        primary key,
    user_id             varchar(20)   null comment '用户id',
    activity_id         varchar(20)   not null comment '动态id',
    comment_content     varchar(1000) null comment '评论内容',
    comment_likes       int default 0 null comment '评论点赞数目',
    comment_create_time datetime      null comment '评论创建时间',
    comment_files       varchar(300)  null comment '评论文件url以逗号分割'
)
    comment '用户评论表';

create index cs_user_activity_comment_activity_id_comment_create_time_index
    on cs_user_activity_comment (activity_id, comment_create_time);

create index cs_user_activity_comment_activity_id_comment_likes_index
    on cs_user_activity_comment (activity_id, comment_likes);

create table cs_user_likes
(
    id             varchar(20) not null comment '主键id'
        primary key,
    cs_comment_id  varchar(20) not null comment '评论id',
    cs_user_id     varchar(20) not null comment '用户id',
    cs_activity_id varchar(20) null comment '动态id',
    constraint cs_comment_likes_cs_comment_id_uindex
        unique (cs_comment_id, cs_user_id)
)
    comment '用户点赞评论记录';

create index cs_user_likes_cs_activity_id_index
    on cs_user_likes (cs_activity_id);

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
)
    charset = utf8;

