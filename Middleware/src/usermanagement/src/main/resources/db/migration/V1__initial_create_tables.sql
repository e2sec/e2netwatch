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

-- definition for table user_status
create table user_status (
    id integer primary key auto_increment
    , code varchar(30) unique not null
    , name varchar(30) not null
    , description varchar(120)
);
    
-- definition for table user
create table user (
    id integer primary key auto_increment
    , username varchar(50) not null
    , password varchar(100) not null
    , first_name varchar(60) not null
    , last_name varchar(60) not null
    , email varchar(255) unique not null
    , ust_id integer
    , constraint uk_usr_username unique (username)
    , constraint fk_usr_ust foreign key(ust_id) references user_status(id) on delete set null
);
    
-- definition for table profile_preference
create table profile_preference (
    id integer primary key auto_increment
    , usr_id integer
    , ugr_id integer
    , timezone varchar(100) not null default 'Europe/Berlin'
    , constraint uk_pps_user unique (usr_id)
    , constraint uk_pps_user_group unique (ugr_id)
    , constraint fk_pps_usr foreign key(usr_id) references user(id) on delete cascade
    , constraint fk_pps_ugr foreign key(ugr_id) references user_group(id) on delete cascade
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
