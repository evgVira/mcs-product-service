create table if not exists t_user
(
    id serial primary key,
    username varchar not null,
    password varchar not null
);

create table t_user_authority
(
    id serial primary key,
    id_user int not null references t_user(id),
    authority varchar not null
);

create table if not exists t_deactivated_token
(
    id uuid primary key,
    keep_until timestamp not null check(keep_until > now())
);