-- Create table
drop table if exists fruit;
create table fruit (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- Init data
insert into fruit(name) values ('Cherry');
insert into fruit(name) values ('Apple');
insert into fruit(name) values ('Banana');