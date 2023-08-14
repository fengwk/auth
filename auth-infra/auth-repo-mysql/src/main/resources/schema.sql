create table if not exists auth_client
(
    id                                 bigint unsigned not null comment '主键',
    gmt_create                         datetime(3) not null default current_timestamp(3) comment '创建时间',
    gmt_modified                       datetime(3) not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
    deleted                            tinyint(1) not null default 0 comment '是否删除，1-是，0-否',
    name                               varchar(32) not null comment '客户端名称',
    description                        varchar(128) comment '客户端描述',
    secret                             varchar(128) not null comment '客户端密钥',
    oauth2_modes                       varchar(128) not null default '[]' comment 'oauth2模式，json数组',
    redirect_uris                      varchar(1024) not null default '[]' comment '重定向uri，json数组',
    authentication_server              varchar(256) not null comment '认证服务器',
    authorization_code_expire_seconds  int not null comment '授权码超时时间，单位秒',
    access_token_expire_seconds        int not null comment '访问令牌超时时间，单位秒',
    refresh_token_expire_seconds       int not null comment '刷新令牌超时时间，单位秒',
    authorization_expire_seconds       int not null comment '授权超时时间，单位秒',
    primary key (id),
    unique uk_name(name),
    index idx_gmtCreate(gmt_create),
    index idx_gmtModified(gmt_modified)
) engine=InnoDB default charset=utf8mb4 comment='OAuth2客户端表';

create table if not exists auth_oauth2_token
(
    id                 bigint unsigned not null comment '主键',
    gmt_create         datetime(3) not null default current_timestamp(3) comment '创建时间',
    gmt_modified       datetime(3) not null default current_timestamp(3) on update current_timestamp(3) comment '修改时间',
    deleted            tinyint(1) not null default 0 comment '是否删除，1-是，0-否',
    client_id          bigint unsigned not null comment '客户端id',
    subject_id         varchar(128) not null comment '授权主体id',
    scope              varchar(256) not null comment '授权范围',
    token_type         varchar(32) not null comment '令牌类型',
    access_token       varchar(128) not null comment '访问令牌',
    refresh_token      varchar(128) not null comment '刷新令牌',
    last_refresh_time  datetime(3) not null comment '最后一次刷新令牌的时间',
    authorization_time datetime(3) not null comment '授权时间',
    primary key (id),
    unique uk_accessToken(access_token),
    unique uk_refreshToken(refresh_token),
    index idx_clientId(client_id),
    index idx_subjectId(subject_id)
    ) engine=InnoDB default charset=utf8mb4 comment='OAuth2授权令牌表';
