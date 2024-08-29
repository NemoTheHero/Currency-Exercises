drop table if exists conversion_rates;
drop table if exists countries;

create table if not exists countries (
                                         ID int not null AUTO_INCREMENT,
                                         country varchar(100) not null,
                                         PRIMARY KEY ( ID )
);

insert into countries (country) values ('USD');
insert into countries (country) values ('GDP');
insert into countries (country) values ('EUR');
insert into countries (country) values ('CAD');
insert into countries (country) values ('YEN');
insert into countries (country) values ('AUS');

create table if not exists conversion_rates(
                                               ID int not null AUTO_INCREMENT,
                                               origin_country_fid int not null,
                                               conversion_country_fid int not null,
                                               conversion_rate NUMERIC(20, 10) not null,
                                               PRIMARY KEY ( ID ),
                                               FOREIGN KEY (origin_country_fid) references countries(ID),
                                               FOREIGN KEY (conversion_country_fid) references countries(ID)
);



insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from countries where country = 'USD'), (select ID from countries where country = 'GDP'), 0.72 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from countries where country = 'EUR'), (select ID from countries where country = 'GDP'), 0.87 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from countries where country = 'CAD'), (select ID from countries where country = 'USD'), 0.78 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from countries where country = 'YEN'), (select ID from countries where country = 'AUS'), 0.012 );
