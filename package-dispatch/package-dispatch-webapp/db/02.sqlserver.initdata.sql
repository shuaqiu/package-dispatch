set identity_insert sys_company on
insert into sys_company(id, code, name, address, parent_id, full_id, type)
values (0, 'huixin', '惠信', 'address', -1, -1, 1)
set identity_insert sys_company off

insert into sys_role(id, code, name)
values (0, 'administrators', '系统管理员')

set identity_insert sys_user on
insert into sys_user(id,  code, name, alias, password, salt, tel, company_id, company, department, address, type, customer_type, state )
values (0, 'admin', '系统管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '', '00000000000', 0, '惠信', '惠信', 'address', 1, null, 1)
set identity_insert sys_user off

insert into sys_user_role(user_id, role_id)
values(0, 0)