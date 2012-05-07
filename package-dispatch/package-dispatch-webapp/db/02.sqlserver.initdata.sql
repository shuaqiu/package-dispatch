set identity_insert sys_company on
insert into sys_company(id, code, name, address, parent_id, full_id, type)
values (0, '000000', '慧信', 'address', -1, -1, 1)
set identity_insert sys_company off

insert into sys_role(id, code, name)
values (0, 'administrators', '系统管理员')

set identity_insert sys_user on
insert into sys_user(id,  code, name, alias, password, salt, tel, company_id, company, department, address, type, customer_type, state )
values (0, '000000', '系统管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '', '00000000000', 0, '慧信', '慧信', '', 1, null, 1)
set identity_insert sys_user off

insert into sys_user_role(user_id, role_id)
values(0, 0)


insert into sys_role(id, code, name)
values (1, 'duty managers', '值班经理')

insert into sys_role(id, code, name)
values (2, 'schedulers', '调度员')

insert into sys_role(id, code, name)
values (3, 'fetchers and deliverers', '收件员/派件员')

insert into sys_role(id, code, name)
values (4, 'transiters', '中转员')


insert into sys_function(id, code, name)
values (101, 'schedule', '订单调度')
insert into sys_function(id, code, name)
values (102, 'alarm', '警报中心')
insert into sys_function(id, code, name)
values (103, 'inStorage', '快件入库')
insert into sys_function(id, code, name)
values (104, 'outStorage', '快件出库')
insert into sys_function(id, code, name)
values (105, 'customerCompany', '客户公司管理')
insert into sys_function(id, code, name)
values (106, 'customer', '客户管理')
insert into sys_function(id, code, name)
values (107, 'employer', '员工管理')

insert into sys_function(id, code, name)
values (201, 'order', '自主下单')
insert into sys_function(id, code, name)
values (202, 'receiverCompany', '收件公司管理')
insert into sys_function(id, code, name)
values (203, 'receiver', '收件人管理')
insert into sys_function(id, code, name)
values (204, 'account', '账号管理')

update sys_function set func_index = id

-- 系统管理员的权限
insert into sys_role_function(role_id, func_id)
select 0, id from sys_function where id > 100 and id < 200

-- 值班经理的权限
insert into sys_role_function(role_id, func_id)
select 1, id from sys_function where id > 100 and id < 200

-- 调度员的权限
insert into sys_role_function(role_id, func_id)
values (2, 101) -- 订单调度
insert into sys_role_function(role_id, func_id)
values (2, 102) -- 警报中心
