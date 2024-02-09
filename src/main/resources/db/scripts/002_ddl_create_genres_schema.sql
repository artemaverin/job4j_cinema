create table if not exists genres
(
    id   serial primary key,
    name varchar unique not null
);