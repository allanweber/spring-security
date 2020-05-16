drop table if exists authorities;
drop table if exists users;
create table users
(
    id        int(11)                 NOT NULL auto_increment primary key,
    username  varchar_ignorecase(50)  not null,
    password  varchar_ignorecase(256) not null,
    email     varchar(100)            not null,
    enabled   boolean                 not null,
    firstname varchar_ignorecase(256) not null,
    lastname  varchar_ignorecase(256) not null,
    verified  boolean                 not null default true
);

create table authorities
(
    id        int(11)                NOT NULL auto_increment primary key,
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);

INSERT into users (username, password, email, enabled, firstname, lastname)
values ('user', '$2a$10$EUxPWS43H55VHJS6OtNdwOVtRwNprMGnwq0sXPhdwVpJo9v5oKYjO', 'user@gmail.com', true, 'User',
        'Admin');

INSERT into users (username, password, email, enabled, firstname, lastname)
values ('admin', '$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu', 'admin@gmail.com', true, 'User',
        'Regular');

INSERT into authorities (username, authority)
values ('user', 'USER');
INSERT into authorities (username, authority)
values ('admin', 'ADMIN');
INSERT into authorities (username, authority)
values ('admin', 'USER');