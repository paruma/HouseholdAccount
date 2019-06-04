# --- First database schema

# --- !Ups
create table household_account (
  id                        bigint not null auto_increment,
  name                      varchar(255) not null,
  price                     bigint not null,
  date                      date not null,
  constraint pk_todo primary key (id))
;
create sequence id_seq start with 1000;

insert into household_account (id, name, price, date) values (1,'本', 1000, '2017-2-3');
insert into household_account (id, name, price, date) values (2,'野菜', 350, '2019-6-3');
insert into household_account (id, name, price, date) values (3,'お肉', 500, '2019-6-4');

# --- !Downs
drop table if exists household_account;

drop sequence if exists id_seq;