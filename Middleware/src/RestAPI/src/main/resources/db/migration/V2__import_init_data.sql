delete from user;
delete from user_group;
delete from authority;
delete from usr_ugr;
delete from ugr_aty;
delete from menu_component;
delete from profile_menu;

insert into user (id, username, password, first_name, last_name, email) values 
    (1, 'admin', '$argon2i$v=19$m=65536,t=2,p=1$8buuloxG+y4CecxZjlKhPw$UDAi/srpZrmgCOny30uhGcMnBJYgBQFECvGB9H/GA44', 'Afirst', 'Alast', 'afirst.alast@mail.com')
    ,(2, 'user', '$argon2i$v=19$m=65536,t=2,p=1$Bzf5J8QwlC/h+HPqJ2np9Q$BRdbtvsoXDQlTEknHSfCs6h+p5A5OV+wq2xwQPEecSk', 'Ufirst', 'Ulast', 'ufirst.ulast@mail.com');
    
insert into user_group (id, name, description) values 
    (1, 'Administrators', 'Description for user group administrators')
    ,(2, 'Users', 'User group users description');
    
insert into authority (id, name) values 
    (1, 'ROLE_ADMIN')
    ,(2, 'ROLE_USER');
    
insert into usr_ugr (usr_id, ugr_id) values 
    ((select id from user where username = 'admin'), (select id from user_group where name = 'Administrators'))
    ,((select id from user where username = 'user'), (select id from user_group where name = 'Users'));
    
insert into ugr_aty (ugr_id, aty_id) values 
    ((select id from user_group where name = 'Administrators'), (select id from authority where name = 'ROLE_ADMIN'))
    ,((select id from user_group where name = 'Users'), (select id from authority where name = 'ROLE_USER'));
    
update profile_preference set default_tzn_id = (select id from timezone where name like '%Berlin%');
insert into profile_preference (id, default_tzn_id, ugr_id) values
    (2, null, 2);