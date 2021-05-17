create table member(
	id int auto_increment primary key,
	email varchar(255),
	password varchar(100),
	name varchar(100),
	regdate datetime,
	unique key (email)
);
