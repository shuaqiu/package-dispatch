set identity_insert sys_company on
insert into sys_company(id, code, name, address, parent_id, full_id, type)
values (1, 'huixin', '惠信', 'address', null, null, 1)
set identity_insert sys_company off

insert into sys_role(id, code, name)
values (1, 'administrators', '系统管理员')

set identity_insert sys_user on
insert into sys_user(id,  code, name, alias, password, tel, group_id, company, department, address, type, customer_type, state )
values (1, 'admin', '系统管理员', 'admin', '', '00000000000', 1, '惠信', 'department', 'address', 1, null, 1)
set identity_insert sys_user off