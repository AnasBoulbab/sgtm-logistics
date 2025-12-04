create table if not exists app_user
(
    user_id    bigserial primary key,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    username   varchar(255) unique,
    password   varchar(255),
    role       varchar(255),
    api_key    varchar(255) unique
);

create table if not exists vehicle
(
    id          bigserial primary key,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    external_id bigint unique,
    source      varchar(255),
    name        varchar(255),
    code        varchar(255) unique,
    type        smallint
);

create table if not exists construction_site
(
    id          bigserial primary key,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    name        varchar(255),
    description varchar(255)
);

create table if not exists day_report
(
    id         bigserial primary key,
    date       date,
    state      smallint,
    work_hours varchar(255),
    vehicle_id bigint,
    constraint fk_day_report_vehicle foreign key (vehicle_id) references vehicle (id),
    constraint uq_day_report_vehicle_date unique (vehicle_id, date)
);

alter table app_user owner to spring_user;
alter table vehicle owner to spring_user;
alter table construction_site owner to spring_user;
alter table day_report owner to spring_user;

insert into app_user (user_id, created_at, updated_at, username, password, api_key, role) values
    (1, now(), now(), 'test api', '$2a$10$GvAP97fOSeopTKDdA4ir2OXDJsEzKQqciQO9euetXmbOzpdSIWsam', 'bavVvPHlG8NhXlASgEGD4B-03Jt-g1ys',
     'ADMIN')
on conflict do nothing;
