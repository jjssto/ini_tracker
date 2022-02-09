-- !Ups

create table rtc_combat
(
    id          int auto_increment
        primary key,
    combat_name varchar(255) null
);

create table rtc_dice_roll
(
    id      bigint auto_increment
        primary key,
    comment varchar(255) null,
    eyes    int          null,
    zeit    datetime(6)  null
);

create table rtc_dice
(
    dice_roll_id bigint not null,
    roll         int    null,
    constraint FKknm5cy8emlmptbvqg2qufvaex2
        foreign key (dice_roll_id) references gen_dice_roll (id)
            on delete cascade
);

create table rtc_dice_rolls
(
    id        bigint auto_increment
        primary key,
    combat_id int    null,
    d12_id    bigint null,
    d8_id     bigint null,
    d4_id     bigint null,
    constraint FK7ln5tv2fk1ph6fehe06b25rqt2
        foreign key (d12_id) references gen_dice_roll (id),
    constraint FK8ugmt8oc69eg5sbt9qb6h3sp62
        foreign key (d4_id) references gen_dice_roll (id),
    constraint FKn4pdyqpmmgycu9qidg8vxpp452
        foreign key (combat_id) references gen_combat (id),
    constraint FKoe4dmy0vo62rwvyx5rgafngm42
        foreign key (d8_id) references gen_dice_roll (id)
);

-- !Downs
drop table rtc_combat;
drop table rtc_dice_roll;
drop table rtc_dice;
drop table rtc_dice_rolls;
