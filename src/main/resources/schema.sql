drop table if exists user_keywords;
drop table if exists user_user_score;
drop table if exists associations;
drop table if exists users;

create table if not exists keywords
(
    ID      int          not null AUTO_INCREMENT,
    keyword varchar(100) not null,
    PRIMARY KEY (ID)
);

create table if not exists associations
(
    ID                     int            not null AUTO_INCREMENT,
    keyword1Id             int            not null,
    keyword2Id             int            not null,
    conversion_rate        NUMERIC(20, 5) not null,
    PRIMARY KEY (ID),
    FOREIGN KEY (keyword1Id) references keywords (ID),
    FOREIGN KEY (keyword2Id) references keywords (ID)
);

create table if not exists users
(
    ID       int          not null AUTO_INCREMENT,
    username varchar(100) not null,
    PRIMARY KEY (ID)
);

create table if not exists user_keywords
(
    ID        int not null AUTO_INCREMENT,
    userId    int not null,
    keywordId int not null,
    PRIMARY KEY (ID),
    FOREIGN KEY (userId) references users (ID),
    FOREIGN KEY (keywordId) references keywords (ID)
);

create table if not exists user_user_score
(
    ID               int            not null AUTO_INCREMENT,
    user1Id         int            not null,
    user2Id         int            not null,
    score           int,
    PRIMARY KEY (ID),
    FOREIGN KEY (user1Id) references users (ID),
    FOREIGN KEY (user2Id) references users (ID)
);

insert into users (ID, username) values (1, 'Nemo');
insert into users (ID, username) values (2, 'Bailey');
insert into users (ID, username) values (3, 'Michael');
insert into users (ID, username) values (4, 'Karim');
insert into users (ID, username) values (5, 'Sam');