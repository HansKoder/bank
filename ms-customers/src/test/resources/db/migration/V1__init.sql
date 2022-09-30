create schema if not exists bank;

create table if not exists bank.customer (
    customer_id SERIAL primary key,
    name varchar not null,
    address varchar not null,
    phone varchar not null,
    password varchar not null,
    enabled boolean not null
);
