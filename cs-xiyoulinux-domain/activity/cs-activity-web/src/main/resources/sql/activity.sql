create table cs_dynamic.cs_user_activity
(
    id                   varchar(20)                        not null comment '主键'
        primary key,
    user_id              varchar(20)                        not null comment '用户Id',
    activity_title       varchar(70)                        not null comment '活动标题',
    activity_content     varchar(2000)                      not null comment '问题or动态or讲座or任务的内容',
    activity_end_time    datetime                           null comment '结束时间',
    activity_status      int                                null,
    activity_type        int                                not null comment '问题or动态or讲座or任务(0/1/2/3)',
    activity_create_time datetime                           not null comment '创建时间',
    activity_files       varchar(700)                       null comment '动态文件url以逗号分割',
    activity_update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint cs_user_activity_id_activity_status_activity_type_uindex
        unique (id, activity_status, activity_type)
)
    comment '用户动态表' charset = utf8;

create index cs_user_activity_activity_create_time_index
    on cs_dynamic.cs_user_activity (activity_create_time);

create index cs_user_activity_user_id_activity_create_time_index
    on cs_dynamic.cs_user_activity (user_id, activity_create_time);

create table cs_dynamic.cs_user_question
(
    id                   varchar(20)   not null comment '主键id'
        primary key,
    question_id          varchar(20)   not null comment '问题id--同动态表中的id',
    question_title       varchar(70)   not null comment '问题标题',
    question_create_time datetime      not null comment '问题创建时间',
    question_status      int           not null comment '问题是否解决-0是未解决/1是解决',
    user_id              varchar(20)   not null comment '用户id',
    question_content     varchar(2000) not null comment '问题内容',
    question_files       varchar(500)  null comment '问题文件url以逗号分割',
    constraint cs_user_question_question_id_uindex
        unique (question_id)
)
    comment '用户问题表';

create table cs_dynamic.cs_user_task
(
    id               varchar(20)   not null comment '主键id'
        primary key,
    user_id          varchar(20)   not null comment '用户id',
    task_id          varchar(20)   not null comment '任务id--同动态表的id',
    task_title       varchar(70)   not null comment '任务标题',
    task_create_time datetime      not null comment '任务创建时间',
    task_end_time    datetime      not null comment '任务的结束时间',
    task_status      int           not null comment '任务的状态',
    task_content     varchar(2000) not null comment '任务内容',
    task_files       varchar(500)  null comment '任务文件url以逗号分割',
    constraint cs_user_task_task_id_uindex
        unique (task_id)
);

create table cs_dynamic.undo_log
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

