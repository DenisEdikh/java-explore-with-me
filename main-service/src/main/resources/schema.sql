drop table if exists users cascade;
drop table if exists categories cascade;

CREATE TABLE IF NOT EXISTS users
(
    id bigint not null generated always as identity,
    name varchar(250) not null,
    email varchar(254) not null,
    constraint pk_user_id primary key (id),
    constraint email_un unique (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id bigint not null generated always as identity,
    name varchar(50) not null,
    constraint pk_cat_id primary key (id),
    constraint name_un unique (name)
);