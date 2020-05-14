drop table if exists authorities;
drop table if exists users;
create table users
(
    username varchar_ignorecase(50) not null primary key,
    password varchar_ignorecase(50) not null,
    email    varchar(100)           not null,
    enabled  boolean                not null
);

create table authorities
(
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);

INSERT into users values ('user','user','user@gmail.com',true);
INSERT into users values ('admin','admin','admin@gmail.com',true);
-- INSERT into users values ('snakamoto','{noop}bitcoin','snakamoto@gmail.com',true);
-- INSERT into users values ('user','{noop}password','user@gmail.com',true);
-- INSERT into users values ('admin','{noop}god','admin@gmail.com',true);
INSERT into authorities values ('user','USER');
INSERT into authorities values ('admin','ADMIN');