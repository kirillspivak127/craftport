create table
    users (
        id bigint generated always as identity primary key,
        username varchar(64) not null unique,
        password_hash varchar(100) not null,
        role varchar(20) not null default 'USER' check (role in ('ADMIN', 'MODERATOR', 'USER')),
        enabled boolean not null default true,
        created_at timestamptz not null default now ()
    );