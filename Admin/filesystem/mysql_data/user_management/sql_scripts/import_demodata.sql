use user_management_db;

delete from user;
delete from user_group;
delete from authority;
delete from usr_ugr;
delete from ugr_aty;
delete from menu;
delete from mnu_ppr;

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

-- inserting into menu table
insert into menu (id, name, url, icon_name, position, super_mnu_id, alignment, name_hidden) values
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
    ,(26, 'Logout', '/logout', 'fa-sign-out', 6, 20, null, null);
alter table menu AUTO_INCREMENT=27;

insert into mnu_ppr (ppr_id, mnu_id) values
    ((select id from profile_preference where usr_id is null and ugr_id is null), 1)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 2)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 3)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 4)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 5)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 6)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 7)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 8)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 9)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 10)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 11)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 12)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 13)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 14)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 15)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 16)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 17)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 18)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 19)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 20)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 21)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 22)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 23)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 24)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 25)
    ,((select id from profile_preference where usr_id is null and ugr_id is null), 26);