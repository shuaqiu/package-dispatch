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

create table dispatch_schedule_history(
    id int not null identity(1,1),
    order_id int not null,
    scheduler_name varchar(255),
    scheduler_tel varchar(20),
    schedule_time datetime
)
alter table dispatch_schedule_history add constraint pk_dispatch_schedule_history primary key (id)


create function fun_getPY(@str nvarchar(4000))
returns nvarchar(4000)
as
begin
declare @word nchar(1),@PY nvarchar(4000)
set @PY=''
while len(@str)>0
begin
set @word=left(@str,1)
--如果非汉字字符，返回原字符
set @PY=@PY+(case when unicode(@word) between 19968 and 19968+20901
then (select top 1 PY from (
select 'A' as PY,N'驁' as word
union all select 'B',N'簿'
union all select 'C',N'錯'
union all select 'D',N'鵽'
union all select 'E',N'樲'
union all select 'F',N'鰒'
union all select 'G',N'腂'
union all select 'H',N'夻'
union all select 'J',N'攈'
union all select 'K',N'穒'
union all select 'L',N'鱳'
union all select 'M',N'旀'
union all select 'N',N'桛'
union all select 'O',N'漚'
union all select 'P',N'曝'
union all select 'Q',N'囕'
union all select 'R',N'鶸'
union all select 'S',N'蜶'
union all select 'T',N'籜'
union all select 'W',N'鶩'
union all select 'X',N'鑂'
union all select 'Y',N'韻'
union all select 'Z',N'咗'
) T
where word>=@word collate Chinese_PRC_CS_AS_KS_WS
order by PY ASC) else @word end)
set @str=right(@str,len(@str)-1)
end
return @PY
end