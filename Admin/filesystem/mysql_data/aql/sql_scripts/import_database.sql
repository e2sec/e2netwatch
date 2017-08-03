drop database if exists aql_db;
create database if not exists aql_db;

use aql_db;

create table ruleset (
	id integer primary key auto_increment
    , rule text not null
    , description varchar(255)
);

create table version (
    version_number integer default 0
);
insert into version (version_number) values (0);
