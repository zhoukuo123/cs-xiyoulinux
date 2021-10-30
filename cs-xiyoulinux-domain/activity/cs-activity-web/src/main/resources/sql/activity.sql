create table cs_user_activity
(
    id                   varchar(20) not null comment '主键'
        primary key,
    user_id              mediumtext  not null comment '用户Id',
    activity_title       varchar(70) not null comment '活动标题',
    activity_content     text        not null comment '问题or动态or讲座or任务的内容',
    activity_end_time    datetime    null     comment  '结束时间',
    activity_status      int         null     comment  '活动状态',
    activity_type        int         not null comment '发起问题or发起动态or发起任务or发起讲座(0/1/2/3)',
    activity_create_time datetime    not null comment '创建时间'
) comment '用户动态表' charset = utf8;

create index cs_user_activity_id_activity_type_activity_status_index
    on cs_user_activity (id, activity_type, activity_status);

create table cs_user_question
(
    id                   varchar(20) not null comment '主键id'
        primary key,
    question_id          varchar(20) not null comment '问题id--同动态表中的id',
    question_title       varchar(70) not null comment '问题标题',
    question_create_time datetime    not null comment '问题创建时间',
    question_status      int         not null comment '问题状态(1, "未解决"、0, "已解决")',
    user_id              varchar(20) not null comment '用户id',
    question_content     text        not null comment '问题内容',
    constraint cs_user_question_question_id_uindex
        unique (question_id)
) comment '用户问题表' charset = utf8;

create index cs_user_question_question_id_question_status_index
    on cs_user_question (question_id, question_status);

create table cs_user_task
(
    id               varchar(20) not null comment '主键id'
        primary key,
    user_id          varchar(20) not null comment '用户id',
    task_id          varchar(20) not null comment '任务id--同动态表的id',
    task_title       varchar(70) not null comment '任务标题',
    task_create_time datetime    not null comment '任务创建时间',
    task_end_time    datetime    not null comment '任务的结束时间',
    task_status      int         not null comment '任务的状态(2, "进行中"、3, "待进行"、4, "已完成")',
    task_content     text        not null comment '任务内容',
    constraint cs_user_task_task_id_uindex
        unique (task_id)
) comment '用户任务表' charset = utf8;

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
