create table sys_company(
    id int not null identity(1,1),
    code varchar(255),
    name varchar(255),
    address varchar(255),
    parent_id int,
    full_id varchar(255),
    type int
)
alter table sys_company add constraint pk_sys_company primary key (id)
go

create table sys_role(
    id int not null,
    code varchar(255),
    name varchar(255)
)
alter table sys_role add constraint pk_sys_role primary key (id)
go

create table sys_user(
    id int not null identity(1,1),
    code varchar(255),
    name varchar(255),
    alias varchar(255),
    password varchar(255),
    tel varchar(20),
    group_id int,
    company varchar(255),
    department varchar(255),
    address varchar(255),
    type int,
    customer_type int,
    state int
)
alter table sys_user add constraint pk_sys_user primary key (id)
go

create table sys_user_role(
    user_id int not null,
    role_id int not null
)
alter table sys_user_role add constraint pk_sys_user_role primary key (user_id, role_id)
go

create table sys_sender_receiver(
    sender_id int not null,
    receiver_id int not null
)
alter table sys_sender_receiver add constraint pk_sys_sender_receiver primary key (sender_id, receiver_id)
go

create table dispatch_order(
    id int not null identity(1,1),
    sender_id int not null,
    sender_code varchar(255),
    sender_name varchar(255),
    sender_tel varchar(20),
    sender_address varchar(255),
    sender_company varchar(255),
    receiver_id int not null,
    receiver_code varchar(255),
    recerver_name varchar(255),
    receiver_tel varchar(20),
    receiver_address varchar(255),
    receiver_company varchar(255),
    order_time datetime,
    goods_name varchar(255),
    quantity varchar(255),
    bar_code varchar(255),
    sender_identity_code varchar(255),
    receiver_identity_code varchar(255),
    start_time datetime,
    end_time datetime,
    scheduler_id int,
    scheduler_name varchar(255),
    scheduler_tel varchar(20),
    schedule_time datetime,
    state int
)
alter table dispatch_order add constraint pk_dispatch_order primary key (id)
go

create table dispatch_schedule_detail(
    id int not null identity(1,1),
    order_id int not null,
    state int,
    handle_index int,
    handler_id int,
    handler_name varchar(255),
    handler_tel varchar(20),
    memo varchar(255)
)
alter table dispatch_schedule_detail add constraint pk_dispatch_schedule_detail primary key (id)
go

create table dispatch_handle_detail(
    id int not null identity(1,1),
    order_id int not null,
    state int,
    handle_index int,
    handler_id int,
    handler_name varchar(255),
    handler_tel varchar(20),
    memo varchar(255),
    handle_time datetime,
    description varchar(255)
)
alter table dispatch_handle_detail add constraint pk_dispatch_handle_detail primary key (id)
go