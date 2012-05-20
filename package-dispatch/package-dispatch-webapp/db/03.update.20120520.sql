if exists (select * from sysobjects where name = 'customer_receiver_company')
  begin
    drop table customer_receiver_company
  end

if exists (select * from sysobjects where name = 'customer_receiver')
  begin
    drop table customer_receiver
  end

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



if not exists (select * from sys_role_function where role_id = 0 and func_id = 203)
  begin
    insert into sys_role_function(role_id, func_id)
    values (0, 203)
  end

if not exists (select * from sys_role_function where role_id = 1 and func_id = 203)
  begin 
    insert into sys_role_function(role_id, func_id)
    values (1, 203)
  end

-- delete the receiver company function
delete from sys_function where id = 202
delete from sys_role_function where func_id = 202