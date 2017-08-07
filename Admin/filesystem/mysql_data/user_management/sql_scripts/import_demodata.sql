use user_management_db;

delete from user;
delete from user_group;
delete from authority;
delete from usr_ugr;
delete from ugr_aty;
delete from menu_component;
delete from profile_menu;

insert into user (username, password, first_name, last_name, email) values 
    ('admin', '$argon2i$v=19$m=65536,t=2,p=1$8buuloxG+y4CecxZjlKhPw$UDAi/srpZrmgCOny30uhGcMnBJYgBQFECvGB9H/GA44', 'John', 'Admin', 'john.admin@e-ito.de')
    ,('user', '$argon2i$v=19$m=65536,t=2,p=1$8buuloxG+y4CecxZjlKhPw$UDAi/srpZrmgCOny30uhGcMnBJYgBQFECvGB9H/GA44', 'Jack', 'User', 'jack.user@e-ito.de');
    
insert into user_group (name, description) values 
    ('Administrators', 'Description for user group administrators')
    ,('Users', 'User group users description');
    
insert into authority (name) values 
    ('ROLE_ADMIN')
    ,('ROLE_USER');
    
insert into usr_ugr (usr_id, ugr_id) values 
    ((select id from user where username = 'admin'), (select id from user_group where name = 'Administrators'))
    ,((select id from user where username = 'user'), (select id from user_group where name = 'Users'));
    
insert into ugr_aty (ugr_id, aty_id) values 
    ((select id from user_group where name = 'Administrators'), (select id from authority where name = 'ROLE_ADMIN'))
    ,((select id from user_group where name = 'Users'), (select id from authority where name = 'ROLE_USER'));
    
update profile_preference set default_tzn_id = (select id from timezone where name like '%Berlin%');

-- inserting into menu_component table
insert into menu_component (id, name, url, icon_name, default_position, default_super_mnc_id, alignment, name_hidden) values
    (1, 'Dashboard', '/dashboard', 'fa-home', 1, null, null, null)
    ,(2, 'Main', '/dashboard/main', 'fa-home', 1, 1, null, null)
    ,(3, 'User', '/dashboard/user', 'user-o', 2, 1, null, null)
    ,(4, 'My great DB', '/dashboard/21', 'fa-exclamation', 3, 1, null, null)
    ,(5, 'Risks', '/risks', 'exclamation-triangle', 2, null, null, null)
    ,(6, 'Policies', '/policies', 'fa-home', 3, null, null, null)
    ,(7, 'Apps/Services', '/apps', 'fa-rocket', 4, null, null, null)
    ,(8, 'Analysis', '/analysis', 'fa-search-plus', 5, null, null, null)
    ,(9, 'Security-events', '/analysis/security-events', 'fa-home', 1, 8, null, null)
    ,(10, 'Host', '/analysis/host', 'fa-user-o', 2, 8, null, null)
    ,(11, 'Events', '/analysis/host/events', 'fa-home', 1, 10, null, null)
    ,(12, 'CMDB', '/analysis/host/cmdb', 'fa-home', 2, 10, null, null)
    ,(13, 'Logs', '/analysis/host/logs', 'fa-home', 3, 10, null, null)
    ,(14, 'Ruleset Editor', '/analysis/ruleset-editor', 'fa-exclamation', 3, 8, null, null)
    ,(15, 'NetFlow', '/analysis/netflow', 'fa-exclamation', 4, 8, null, null)
    ,(16, 'LARA', '/analysis/lara', 'fa-exclamation', 5, 8, null, null)
    ,(17, 'Logs', '/analysis/logs', 'fa-exclamation', 6, 8, null, null)
    ,(18, 'Reports', '/analysis/Reports', 'fa-exclamation', 7, 8, null, null)
    ,(19, 'Alert', null, 'fa-exclamation', 6, null, 'right', null)
    ,(20, 'UserMenu', null, null, 7, null, 'right', true)
    ,(21, 'Profil', '/user', 'fa-user-o', 1, 20, null, null)
    ,(22, 'Change password', '/changePassword', 'fa-unlock-alt', 2, 20, null, null)
    ,(23, 'Administration', '/administration', 'fa-cogs', 3, 20, null, null)
    ,(24, 'Usermanagement', '/usermanagement', 'fa-users', 4, 20, null, null)
    ,(25, 'Konfiguration', '/usermanagement', 'fa-wrench', 5, 20, null, null)
    ,(26, 'Logout', '/logout', 'fa-sign-out', 6, 20, null, null)
    ,(27, '[divider]', null, null, null, null, null, null);
alter table menu_component AUTO_INCREMENT=28;

insert into profile_menu (id, ppr_id, mnc_id, position, super_prm_id) values
    (1, 1, 1, 1, null)
    ,(2, null, 2, 1, 1)
    ,(3, null, 3, 2, 1)
    ,(4, null, 4, 3, 1)
    ,(5, 1, 5, 2, null)
    ,(6, 1, 6, 3, null)
    ,(7, 1, 7, 4, null)
    ,(8, 1, 8, 5, null)
    ,(9, null, 9, 1, 8)
    ,(10, null, 10, 2, 8)
    ,(11, null, 11, 1, 10)
    ,(12, null, 12, 2, 10)
    ,(13, null, 13, 3, 10)
    ,(14, null, 14, 3, 8)
    ,(15, null, 15, 4, 8)
    ,(16, null, 16, 5, 8)
    ,(17, null, 17, 6, 8)
    ,(18, null, 18, 7, 8)
    ,(19, 1, 19, 6, null)
    ,(20, 1, 20, 7, null)
    ,(21, null, 21, 1, 20)
    ,(22, null, 22, 2, 20)
    ,(23, null, 27, 3, 20)
    ,(24, null, 23, 4, 20)
    ,(25, null, 24, 5, 20)
    ,(26, null, 25, 6, 20)
    ,(27, null, 27, 7, 20)
    ,(28, null, 26, 8, 20);
alter table profile_menu AUTO_INCREMENT=29;