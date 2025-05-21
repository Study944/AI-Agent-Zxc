create table if not exists `memory`
(
    `id` int(11)  auto_increment primary key,
    `conversation_id` varchar(64) not null ,
    `role`            varchar(32) not null comment 'user / assistant / system',
    `content`         text        not null,
    `create_time`      datetime    not null default current_timestamp comment '创建时间'
);