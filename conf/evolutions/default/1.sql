-- !Ups

create table gen_combat
(
    id          int auto_increment
        primary key,
    combat_name varchar(255) null
);

create table gen_dice_roll
(
    id      bigint auto_increment
        primary key,
    comment varchar(255) null,
    eyes    int          null,
    zeit    datetime(6)  null
);

create table gen_dice
(
    dice_roll_id bigint not null,
    roll         int    null,
    constraint FKknm5cy8emlmptbvqg2qufvaex
        foreign key (dice_roll_id) references gen_dice_roll (id)
        on delete cascade
);

create table gen_dice_rolls
(
    id        bigint auto_increment
        primary key,
    combat_id int    null,
    d10_id    bigint null,
    d12_id    bigint null,
    d20_id    bigint null,
    d4_id     bigint null,
    d6_id     bigint null,
    d8_id     bigint null,
    constraint FK7ln5tv2fk1ph6fehe06b25rqt
        foreign key (d12_id) references gen_dice_roll (id),
    constraint FK8ugmt8oc69eg5sbt9qb6h3sp6
        foreign key (d4_id) references gen_dice_roll (id),
    constraint FKcgi1256ip6od47y95ng2v4bnr
        foreign key (d10_id) references gen_dice_roll (id),
    constraint FKn4pdyqpmmgycu9qidg8vxpp45
        foreign key (combat_id) references gen_combat (id),
    constraint FKnn4prpc93bd5l3jdl195rfnhd
        foreign key (d6_id) references gen_dice_roll (id),
    constraint FKoe4dmy0vo62rwvyx5rgafngm4
        foreign key (d8_id) references gen_dice_roll (id),
    constraint FKq4vda95g0hpxgeyxse850y8ac
        foreign key (d20_id) references gen_dice_roll (id)
);

create table permission
(
    id    int auto_increment
        primary key,
    value varchar(255) null
);

create table security_role
(
    id        int auto_increment
        primary key,
    role_name varchar(255) null
);

create table sr4_char
(
    id        int auto_increment
        primary key,
    ini       int          null,
    intuition int          null,
    char_name varchar(255) null,
    p_boxes   int          null,
    pc        bit          null,
    reaction  int          null,
    s_boxes   int          null
);

create table sr4_combat
(
    id           int auto_increment
        primary key,
    combat_desc  varchar(255) null,
    last_changed datetime(6)  null
);

create table sr4_char_record
(
    id        int auto_increment
        primary key,
    ini_value int null,
    local_ini int null,
    p_dmg     int null,
    s_dmg     int null,
    char_id   int null,
    combat_id int null,
    constraint FK2itc90kfj9rtdt34r31i0obx
        foreign key (combat_id) references sr4_combat (id)
        on delete cascade,
    constraint FK9q52jgqaugy29lkf3d3xvvwof
        foreign key (char_id) references sr4_char (id)
        on delete cascade
);

create table sr4_dice_roll
(
    id            bigint auto_increment
        primary key,
    comment       varchar(255) null,
    eyes          int          null,
    zeit          datetime(6)  null,
    sr4_record_id int          null,
    constraint FK1l5kx4xa30x6sqlq8lrsuqimt
        foreign key (sr4_record_id) references sr4_char_record (id)
        on delete cascade
);

create table sr4_dice
(
    dice_roll_id bigint not null,
    roll         int    null,
    constraint FKqw8ykwonooo0uulhtnodwm8vq
        foreign key (dice_roll_id) references sr4_dice_roll (id)
        on delete cascade
);

create table user
(
    id            int auto_increment
        primary key,
    password_hash longblob     null,
    salt          varchar(255) null,
    user_name     varchar(255) null
);

create table session_token
(
    id      int auto_increment
        primary key,
    created datetime(6)  null,
    token   varchar(255) null,
    user_id int          null,
    constraint FKaop51xhbhthlbhkphrg5adspt
        foreign key (user_id) references user (id)
        on delete cascade
);

create table user_permission
(
    user_id       int not null,
    permission_id int not null,
    constraint FK7c2x74rinbtf33lhdcyob20sh
        foreign key (user_id) references user (id),
    constraint FKbklmo9kchans5u3e4va0ouo1s
        foreign key (permission_id) references permission (id)
);

create table user_security_role
(
    user_id          int not null,
    security_role_id int not null,
    constraint FK7ai658kmv4m9empfud3p5cirr
        foreign key (security_role_id) references security_role (id),
    constraint FKiakpcnw52x36ddp6ii054abrn
        foreign key (user_id) references user (id)
);

insert into sr4_char( ini, intuition, char_name, pc, reaction)
values
    (  10, 10, 'Max', 0, 10 ),
    (  10, 10, 'Anna', 0, 10 );

insert into sr4_combat(combat_desc, last_changed )
values ( 'Test', now() );

-- !Downs
drop table gen_dice_rolls;
drop table gen_dice;
drop table gen_dice_roll;
drop table gen_combat;
drop table session_token;
drop table sr4_dice;
drop table sr4_dice_roll;
drop table sr4_char_record;
drop table sr4_combat;
drop table sr4_char;
drop table user_permission;
drop table user_security_role;
drop table security_role;
drop table user;
drop table permission;

