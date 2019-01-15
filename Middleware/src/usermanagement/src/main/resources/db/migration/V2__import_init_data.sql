delete from user;
delete from user_group;
delete from authority;
delete from usr_ugr;
delete from ugr_aty;

insert into user_status (code, name, description) values 
    ('ACTIVE', 'Active', 'Status for users who are active')
    ,('NOT_ACTIVE', 'Not active', 'Status for users who are not active');

insert into user (id, username, password, first_name, last_name, email, ust_id) values 
    (1, 'admin', '$argon2i$v=19$m=65536,t=2,p=1$8buuloxG+y4CecxZjlKhPw$UDAi/srpZrmgCOny30uhGcMnBJYgBQFECvGB9H/GA44', 
    'Afirst', 'Alast', 'afirst.alast@mail.com', (select id from user_status where code = 'ACTIVE'))
    ,(2, 'user', '$argon2i$v=19$m=65536,t=2,p=1$Bzf5J8QwlC/h+HPqJ2np9Q$BRdbtvsoXDQlTEknHSfCs6h+p5A5OV+wq2xwQPEecSk', 
    'Ufirst', 'Ulast', 'ufirst.ulast@mail.com', (select id from user_status where code = 'ACTIVE'));
    
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
    
insert into profile_preference (timezone) values
    ('Europe/Berlin');