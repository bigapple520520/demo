--用户表，记录登陆成功的用户名和密码
create table login_user(
    username varchar(30) not null,
    password varchar(100) null,
    creation_time datetime not null,
    modify_time datetime not null,
    primary key(username)
)
go