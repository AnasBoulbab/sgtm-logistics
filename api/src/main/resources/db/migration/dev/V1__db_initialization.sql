create table if not exists app_user
(
    user_id  bigserial primary key,
    created_at timestamp default CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP,
    username varchar(255) unique,
    password varchar(255),
    api_key  varchar(255),
    role     varchar(255)
);

insert into app_user values
    (1, now(), now(), 'test', '$2a$10$GvAP97fOSeopTKDdA4ir2OXDJsEzKQqciQO9euetXmbOzpdSIWsam', 'bavVvPHlG8NhXlASgEGD4B-03Jt-g1ys', 'ADMIN')
on conflict do nothing;