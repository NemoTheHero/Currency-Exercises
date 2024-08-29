drop table if exists conversion_rates;
drop table if exists country;

create table if not exists country (
                                         ID int not null AUTO_INCREMENT,
                                         country varchar(100) not null,
                                         PRIMARY KEY ( ID )
);

insert into country (country) values ('USD');
insert into country (country) values ('GDP');
insert into country (country) values ('EUR');
insert into country (country) values ('CAD');
insert into country (country) values ('YEN');
insert into country (country) values ('AUS');

create table if not exists conversion_rates(
                                               ID int not null AUTO_INCREMENT,
                                               origin_country_fid int not null,
                                               conversion_country_fid int not null,
                                               conversion_rate NUMERIC(20, 10) not null,
                                               PRIMARY KEY ( ID ),
                                               FOREIGN KEY (origin_country_fid) references country(ID),
                                               FOREIGN KEY (conversion_country_fid) references country(ID)
);



insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from country where country = 'USD'), (select ID from country where country = 'GDP'), 0.72 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from country where country = 'EUR'), (select ID from country where country = 'GDP'), 0.87 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from country where country = 'CAD'), (select ID from country where country = 'USD'), 0.78 );

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ( (select ID from country where country = 'YEN'), (select ID from country where country = 'AUS'), 0.012 );
