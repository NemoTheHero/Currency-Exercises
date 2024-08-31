drop table if exists conversion_rates;
drop table if exists country;
drop table if exists income_tax_brackets;

create table if not exists country
(
    ID      int          not null AUTO_INCREMENT,
    country varchar(100) not null,
    PRIMARY KEY (ID)
);

insert into country (country)
values ('USD');
insert into country (country)
values ('GDP');
insert into country (country)
values ('EUR');
insert into country (country)
values ('CAD');
insert into country (country)
values ('YEN');
insert into country (country)
values ('AUS');
insert into country (country)
values ('MEX');
insert into country (country)
values ('CHF');
insert into country (country)
values ('INR');

create table if not exists conversion_rates
(
    ID                     int            not null AUTO_INCREMENT,
    origin_country_fid     int            not null,
    conversion_country_fid int            not null,
    conversion_rate        NUMERIC(20, 5) not null,
    PRIMARY KEY (ID),
    FOREIGN KEY (origin_country_fid) references country (ID),
    FOREIGN KEY (conversion_country_fid) references country (ID)
);



insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'USD'), (select ID from country where country = 'GDP'), 0.72);

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'EUR'), (select ID from country where country = 'GDP'), 0.87);

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'CAD'), (select ID from country where country = 'USD'), 0.78);

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'YEN'), (select ID from country where country = 'AUS'), 0.012);

insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'USD'), (select ID from country where country = 'CAD'), 0.50);
insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'MEX'), (select ID from country where country = 'GDP'), 0.8);
insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'CHF'), (select ID from country where country = 'MEX'), 0.5);
insert into conversion_rates (origin_country_fid, conversion_country_fid, conversion_rate)
VALUES ((select ID from country where country = 'INR'), (select ID from country where country = 'MEX'), 2);

create table if not exists income_tax_brackets
(
    ID           int            not null AUTO_INCREMENT,
    lower_limit  NUMERIC(20, 5) not null,
    higher_limit NUMERIC(20, 5) not null,
    tax_rate     NUMERIC(20, 5) not null,
    PRIMARY KEY (ID)
);

insert into income_tax_brackets (lower_limit, higher_limit, tax_rate)
VALUES (0, 20000, .10);
insert into income_tax_brackets (lower_limit, higher_limit, tax_rate)
VALUES (20000, 50000, .15);
insert into income_tax_brackets (lower_limit, higher_limit, tax_rate)
VALUES (50000, 100000, .20);
insert into income_tax_brackets (lower_limit, higher_limit, tax_rate)
VALUES (150000, 100000000000000, .30);
insert into income_tax_brackets (lower_limit, higher_limit, tax_rate)
VALUES (100000, 150000, .25);
