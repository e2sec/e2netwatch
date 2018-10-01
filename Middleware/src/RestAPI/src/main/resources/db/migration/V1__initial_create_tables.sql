-- definition for table timezone
create table timezone (
    id integer primary key auto_increment
    , name varchar(50) not null
    , offset decimal(4,2) not null
    , constraint uk_tzn_name unique (name)
);
-- code values for table timezone
insert into timezone (offset, name) values
    (-0, '(GMT-10:00) Hawaii'),
    (-9, '(GMT-09:00) Alaska'),
    (-8, '(GMT-08:00) Pacific Time (US &amp; Canada)'),
    (-7, '(GMT-07:00) Arizona'),
    (-7, '(GMT-07:00) Mountain Time (US &amp; Canada)'),
    (-6, '(GMT-06:00) Central Time (US &amp; Canada)'),
    (-5, '(GMT-05:00) Eastern Time (US &amp; Canada)'),
    (-5, '(GMT-05:00) Indiana (East)'),
    (-12, '(GMT-12:00) International Date Line West'),
    (-11, '(GMT-11:00) Midway Island'),
    (-11, '(GMT-11:00) Samoa'),
    (-8, '(GMT-08:00) Tijuana'),
    (-7, '(GMT-07:00) Chihuahua'),
    (-7, '(GMT-07:00) La Paz'),
    (-7, '(GMT-07:00) Mazatlan'),
    (-6, '(GMT-06:00) Central America'),
    (-6, '(GMT-06:00) Guadalajara'),
    (-6, '(GMT-06:00) Mexico City'),
    (-6, '(GMT-06:00) Monterrey'),
    (-6, '(GMT-06:00) Saskatchewan'),
    (-5, '(GMT-05:00) Bogota'),
    (-5, '(GMT-05:00) Lima'),
    (-5, '(GMT-05:00) Quito'),
    (-4, '(GMT-04:00) Atlantic Time (Canada)'),
    (-4, '(GMT-04:00) Caracas'),
    (-4, '(GMT-04:00) La Paz'),
    (-4, '(GMT-04:00) Santiago'),
    (-3.5, '(GMT-03:30) Newfoundland'),
    (-3, '(GMT-03:00) Brasilia'),
    (-3, '(GMT-03:00) Buenos Aires'),
    (-3, '(GMT-03:00) Georgetown'),
    (-3, '(GMT-03:00) Greenland'),
    (-2, '(GMT-02:00) Mid-Atlantic'),
    (-1, '(GMT-01:00) Azores'),
    (-1, '(GMT-01:00) Cape Verde Is.'),
    (0, '(GMT) Casablanca'),
    (0, '(GMT) Dublin'),
    (0, '(GMT) Edinburgh'),
    (0, '(GMT) Lisbon'),
    (0, '(GMT) London'),
    (0, '(GMT) Monrovia'),
    (1, '(GMT+01:00) Amsterdam'),
    (1, '(GMT+01:00) Belgrade'),
    (1, '(GMT+01:00) Berlin'),
    (1, '(GMT+01:00) Bern'),
    (1, '(GMT+01:00) Bratislava'),
    (1, '(GMT+01:00) Brussels'),
    (1, '(GMT+01:00) Budapest'),
    (1, '(GMT+01:00) Copenhagen'),
    (1, '(GMT+01:00) Ljubljana'),
    (1, '(GMT+01:00) Madrid'),
    (1, '(GMT+01:00) Paris'),
    (1, '(GMT+01:00) Prague'),
    (1, '(GMT+01:00) Rome'),
    (1, '(GMT+01:00) Sarajevo'),
    (1, '(GMT+01:00) Skopje'),
    (1, '(GMT+01:00) Stockholm'),
    (1, '(GMT+01:00) Vienna'),
    (1, '(GMT+01:00) Warsaw'),
    (1, '(GMT+01:00) West Central Africa'),
    (1, '(GMT+01:00) Zagreb'),
    (2, '(GMT+02:00) Athens'),
    (2, '(GMT+02:00) Bucharest'),
    (2, '(GMT+02:00) Cairo'),
    (2, '(GMT+02:00) Harare'),
    (2, '(GMT+02:00) Helsinki'),
    (2, '(GMT+02:00) Istanbul'),
    (2, '(GMT+02:00) Jerusalem'),
    (2, '(GMT+02:00) Kyev'),
    (2, '(GMT+02:00) Minsk'),
    (2, '(GMT+02:00) Pretoria'),
    (2, '(GMT+02:00) Riga'),
    (2, '(GMT+02:00) Sofia'),
    (2, '(GMT+02:00) Tallinn'),
    (2, '(GMT+02:00) Vilnius'),
    (3, '(GMT+03:00) Baghdad'),
    (3, '(GMT+03:00) Kuwait'),
    (3, '(GMT+03:00) Moscow'),
    (3, '(GMT+03:00) Nairobi'),
    (3, '(GMT+03:00) Riyadh'),
    (3, '(GMT+03:00) St. Petersburg'),
    (3, '(GMT+03:00) Volgograd'),
    (3.5, '(GMT+03:30) Tehran'),
    (4, '(GMT+04:00) Abu Dhabi'),
    (4, '(GMT+04:00) Baku'),
    (4, '(GMT+04:00) Muscat'),
    (4, '(GMT+04:00) Tbilisi'),
    (4, '(GMT+04:00) Yerevan'),
    (4.5, '(GMT+04:30) Kabul'),
    (5, '(GMT+05:00) Ekaterinburg'),
    (5, '(GMT+05:00) Islamabad'),
    (5, '(GMT+05:00) Karachi'),
    (5, '(GMT+05:00) Tashkent'),
    (5.5, '(GMT+05:30) Chennai'),
    (5.5, '(GMT+05:30) Kolkata'),
    (5.5, '(GMT+05:30) Mumbai'),
    (5.5, '(GMT+05:30) New Delhi'),
    (5.75, '(GMT+05:45) Kathmandu'),
    (6, '(GMT+06:00) Almaty'),
    (6, '(GMT+06:00) Astana'),
    (6, '(GMT+06:00) Dhaka'),
    (6, '(GMT+06:00) Novosibirsk'),
    (6, '(GMT+06:00) Sri Jayawardenepura'),
    (6.5, '(GMT+06:30) Rangoon'),
    (7, '(GMT+07:00) Bangkok'),
    (7, '(GMT+07:00) Hanoi'),
    (7, '(GMT+07:00) Jakarta'),
    (7, '(GMT+07:00) Krasnoyarsk'),
    (8, '(GMT+08:00) Beijing'),
    (8, '(GMT+08:00) Chongqing'),
    (8, '(GMT+08:00) Hong Kong'),
    (8, '(GMT+08:00) Irkutsk'),
    (8, '(GMT+08:00) Kuala Lumpur'),
    (8, '(GMT+08:00) Perth'),
    (8, '(GMT+08:00) Singapore'),
    (8, '(GMT+08:00) Taipei'),
    (8, '(GMT+08:00) Ulaan Bataar'),
    (8, '(GMT+08:00) Urumqi'),
    (9, '(GMT+09:00) Osaka'),
    (9, '(GMT+09:00) Sapporo'),
    (9, '(GMT+09:00) Seoul'),
    (9, '(GMT+09:00) Tokyo'),
    (9, '(GMT+09:00) Yakutsk'),
    (9.5, '(GMT+09:30) Adelaide'),
    (9.5, '(GMT+09:30) Darwin'),
    (10, '(GMT+10:00) Brisbane'),
    (10, '(GMT+10:00) Canberra'),
    (10, '(GMT+10:00) Guam'),
    (10, '(GMT+10:00) Hobart'),
    (10, '(GMT+10:00) Melbourne'),
    (10, '(GMT+10:00) Port Moresby'),
    (10, '(GMT+10:00) Sydney'),
    (10, '(GMT+10:00) Vladivostok'),
    (11, '(GMT+11:00) Magadan'),
    (11, '(GMT+11:00) New Caledonia'),
    (11, '(GMT+11:00) Solomon Is.'),
    (12, '(GMT+12:00) Auckland'),
    (12, '(GMT+12:00) Fiji'),
    (12, '(GMT+12:00) Kamchatka'),
    (12, '(GMT+12:00) Marshall Is.'),
    (12, '(GMT+12:00) Wellington'),
    (13, '(GMT+13:00) Nukualofa');
    
-- definition for table authority
create table authority (
    id integer primary key auto_increment
    , name varchar(50) not null
    , description varchar(120)
    , constraint uk_aty_name unique (name)
);
    
-- definition for table user_group
create table user_group (
    id integer primary key auto_increment
    , name varchar(30) not null
    , description varchar(120)
    , constraint uk_ugr_name unique (name)
);
    
-- definition for table user
create table user (
    id integer primary key auto_increment
    , username varchar(50) not null
    , password varchar(100) not null
    , first_name varchar(60) not null
    , last_name varchar(60) not null
    , email varchar(255) not null
    , constraint uk_usr_username unique (username)
);
    
-- definition for table profile_preference
create table profile_preference (
    id integer primary key auto_increment
    , usr_id integer
    , ugr_id integer
    , default_tzn_id integer
    , constraint uk_pps_user unique (usr_id)
    , constraint uk_pps_user_group unique (ugr_id)
    , constraint fk_pps_usr foreign key(usr_id) references user(id) on delete cascade
    , constraint fk_pps_ugr foreign key(ugr_id) references user_group(id) on delete cascade
    , constraint fk_pps_tzn foreign key(default_tzn_id) references timezone(id) on delete set null
);
-- global profile preferences
insert into profile_preference (default_tzn_id) values
    (null);
    
-- definition for table usr_aty
create table usr_aty (
    id integer primary key auto_increment
    , usr_id integer not null
    , aty_id integer not null
    , constraint uk_usr_aty_1 unique (usr_id, aty_id)
    , constraint fk_usr_aty_usr foreign key(usr_id) references user(id) on delete cascade
    , constraint fk_usr_aty_aty foreign key(aty_id) references authority(id) on delete cascade
);
    
-- definition for table usr_ugr
create table usr_ugr (
    id integer primary key auto_increment
    , usr_id integer not null
    , ugr_id integer not null
    , constraint uk_usr_ugr_1 unique (usr_id, ugr_id)
    , constraint fk_usr_ugr_usr foreign key(usr_id) references user(id) on delete cascade
    , constraint fk_usr_ugr_ugr foreign key(ugr_id) references user_group(id) on delete cascade
);
    
-- definition for table ugr_aty
create table ugr_aty (
    id integer primary key auto_increment
    , aty_id integer not null
    , ugr_id integer not null
    , constraint uk_ugr_aty_1 unique (aty_id, ugr_id)
    , constraint fk_ugr_aty_aty foreign key(aty_id) references authority(id) on delete cascade
    , constraint fk_ugr_aty_ugr foreign key(ugr_id) references user_group(id) on delete cascade
);
    
-- definition for table menu_component
create table menu_component (
    id integer primary key auto_increment
    , name varchar(15) not null
    , url varchar(150)
    , icon_name varchar(50)
    , alignment varchar(10)
    , name_hidden boolean
    , default_position integer
    , default_super_mnc_id integer
    , constraint fk_mnc_mnc foreign key(default_super_mnc_id) references menu_component(id) on delete cascade
);
    
-- definition for table mnu_ppr
create table profile_menu (
    id integer primary key auto_increment
    , ppr_id integer
    , mnc_id integer not null
    , position integer
    , super_prm_id integer
    , constraint fk_prm_ppr foreign key(ppr_id) references profile_preference(id) on delete cascade
    , constraint fk_prm_mnc foreign key(mnc_id) references menu_component(id) on delete cascade
    , constraint fk_prm_prm foreign key(super_prm_id) references profile_menu(id) on delete cascade
);
