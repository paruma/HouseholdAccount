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

insert into household_account (id, name, price, date) values (1,'本', 1000, '1900-1-1');
insert into household_account (id, name, price, date) values (2,'食品', 350, '1900-1-2');

# --- !Downs
drop table if exists household_account;

drop sequence if exists id_seq;