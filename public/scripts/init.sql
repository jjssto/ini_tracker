drop database ini_tracker_db;
commit;
create database ini_tracker_db;
grant all on ini_tracker_db.* to ini_tracker_user@localhost;
flush privileges;
commit;

use ini_tracker_db;
show tables;

create table sr4_char
(
    id        integer primary key auto_increment,
    ini       integer      not null default 0,
    intuition integer      not null default 0,
    reaction  integer      not null default 0,
    char_name varchar(255) not null default '',
    p_boxes   integer      not null default 0,
    s_boxes   integer      not null default 0,
    pc        bit(1)       not null default 0
);

create table combat (
    id integer primary key auto_increment,
    combat_desc varchar(255) not null default ''
);

create table char_record
(
    id integer primary key auto_increment,
    ini_value integer not null default 0,
    local_ini integer not null default 0,
    p_dmg integer not null default 0,
    s_dmg integer not null default 0,
    char_id integer,
    combat_id integer,
    constraint foreign key fk_char_record_char_id ( char_id )
                         references sr4_char( id ),
    constraint foreign key fk_char_record_combat_id ( combat_id )
        references combat( id )
);

create table dice_roll
(
    id bigint primary key auto_increment,
    comment varchar(255),
    eyes int not null default 6,
    zeit datetime(6) not null,
    char_record_id integer not null,
    constraint foreign key fk_dice_roll_char_record_id ( char_record_id )
        references char_record( id )
        on delete cascade
);

create table dice
(
    dice_roll_id bigint,
    roll integer not null,
    constraint foreign key fk_dice_dice_roll_id ( dice_roll_id )
        references dice_roll ( id )
        on delete cascade
);


create table security_role
(
    id integer primary key auto_increment,
    role_name varchar(255)
);
insert into security_role ( id, role_name )
values
    ( 1, 'Admin'),
    ( 2, 'User');

create table permission (
    id integer primary key auto_increment,
    value varchar(255)
);

create table user (
    id integer primary key auto_increment,
    user_name varchar(255)
);

create table user_permission (
   user_id integer,
   permission_id integer,
   constraint foreign key fk_user_permission_user_id ( user_id )
                             references user ( id ),
   constraint foreign key fk_user_permission_permission_id ( permission_id )
                             references  permission ( id )
);

create table user_security_role (
    user_id integer,
    security_role_id integer,
    constraint foreign key fk_user_security_role_user_id ( user_id )
                                references user ( id ) 
                                on delete cascade,
    constraint foreign key fk_user_security_role_security_role_id ( security_role_id )
                                references security_role ( id )
                                on delete cascade
);


commit;