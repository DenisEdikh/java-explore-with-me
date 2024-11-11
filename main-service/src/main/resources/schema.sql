drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists events cascade;
drop table if exists requests cascade;
drop table if exists compilations cascade;
drop table if exists event_compilation cascade;

CREATE TABLE IF NOT EXISTS users
(
    id    bigint       not null generated always as identity,
    name  varchar(250) not null,
    email varchar(254) not null,
    constraint pk_user_id primary key (id),
    constraint email_un unique (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   bigint      not null generated always as identity,
    name varchar(50) not null,
    constraint pk_cat_id primary key (id),
    constraint name_un unique (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id            bigint                      not null generated always as identity,
    title         varchar(120)                not null,
    annotation    varchar(2000)               not null,
    description   varchar(7000)               not null,
    state         varchar(10)                 not null,
    event_date    timestamp without time zone not null,
    created_on    timestamp without time zone not null,
    published_on  timestamp without time zone null,
    paid          bool                        not null default false,
    part_limit    int                         not null default 0,
    conf_requests int                         not null,
    req_moder     bool                        not null default true,
    cat_id        bigint                      not null,
    user_id       bigint                      not null,
    lat           real                        not null,
    lon           real                        not null,
    constraint pk_event_id primary key (id),
    constraint fk_cat_id foreign key (cat_id) references categories (id),
    constraint fk_user_id foreign key (user_id) references users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           bigint                      not null generated always as identity,
    created      timestamp without time zone not null,
    event_id     bigint                      not null,
    requester_id bigint                      not null,
    status       varchar(10)                 not null,
    constraint pk_request_id primary key (id),
    constraint fk_requester_id foreign key (requester_id) references users (id),
    constraint fk_event_id foreign key (event_id) references events (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     bigint      not null generated always as identity,
    pinned bool        null,
    title  varchar(50) null,
    constraint pk_compilation_id primary key (id),
    constraint title_un unique (title)
);

CREATE TABLE IF NOT EXISTS event_compilation
(
    event_id       bigint null,
    compilation_id bigint null,
    constraint fk_event_id foreign key (event_id) references events (id),
    constraint fk_compilation_id foreign key (compilation_id) references compilations (id),
    constraint pk_event_compilation_id primary key (event_id, compilation_id)
);