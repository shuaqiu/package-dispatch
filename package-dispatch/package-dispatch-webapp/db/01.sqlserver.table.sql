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

create table sys_role(
    id int not null,
    code varchar(255),
    name varchar(255)
)
alter table sys_role add constraint pk_sys_role primary key (id)

create table sys_user(
    id int not null identity(1,1),
    code varchar(255),
    name varchar(255),
    login_account varchar(255),
    password varchar(255),
    salt varchar(255),
    tel varchar(20),
    short_number varchar(20),
    company_id int,
    company varchar(255),
    department varchar(255),
    address varchar(255),
    type int,
    customer_type int,
    state int
)
alter table sys_user add constraint pk_sys_user primary key (id)

create table sys_user_role(
    user_id int not null,
    role_id int not null
)
alter table sys_user_role add constraint pk_sys_user_role primary key (user_id, role_id)


create table sys_function(
    id int not null,
    code varchar(255),
    name varchar(255),
    parent_id int,
    func_index int
)
alter table sys_function add constraint pk_sys_function primary key (id)

create table sys_role_function(
    role_id int not null,
    func_id int not null
)
alter table sys_role_function add constraint pk_sys_role_function primary key (role_id, func_id)



create table customer_receiver(
    id int not null identity(1,1),
    user_company_id int,
    user_company varchar(255),
    name varchar(255),
    tel varchar(20),
    company varchar(255),
    address varchar(255)
)
alter table customer_receiver add constraint pk_customer_receiver primary key (id)


create table dispatch_order(
    id int not null identity(1,1),
    
    -- from sys_user
    sender_id int not null,
    sender_name varchar(255),
    sender_tel varchar(20),
    sender_company varchar(255),
    sender_address varchar(255),
    
    -- from customer_receiver
    receiver_id int not null,
    receiver_name varchar(255),
    receiver_tel varchar(20),
    receiver_company varchar(255),
    receiver_address varchar(255),
    
    order_time datetime default getdate(),
    goods_name varchar(255),
    quantity varchar(255),
    
    bar_code varchar(255),
    sender_identity_code varchar(255),
    receiver_identity_code varchar(255),
    
    fetch_time datetime,
    deliver_time datetime,
    
    scheduler_id int,
    scheduler_name varchar(255),
    scheduler_tel varchar(20),
    schedule_time datetime,
    
    handler_id int,
    handler_name varchar(255),
    handler_tel varchar(20),
    
    state int,
    state_describe varchar(255)
)
alter table dispatch_order add constraint pk_dispatch_order primary key (id)


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
    schedule_id int,
    description varchar(255)
)
alter table dispatch_handle_detail add constraint pk_dispatch_handle_detail primary key (id)
