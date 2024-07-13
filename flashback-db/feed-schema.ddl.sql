DROP TABLE IF EXISTS direction_data;
DROP TABLE IF EXISTS directors_data;
DROP TABLE IF EXISTS movies_data;

SET client_encoding to 'UTF8';

CREATE TABLE direction_data (
	movie_id int4 NULL,
	director_id int4 NULL
);

CREATE TABLE directors_data (
	firstname varchar(16) NULL,
	lastname varchar(16) NULL,
	director_code varchar(32) NULL
);

CREATE TABLE movies_data (
	isan varchar(64) NULL,
	title varchar(64) NULL,
	genre_id int4 NULL,
	release_year int4 NULL,
	poster varchar(64) NULL,
	trailer varchar(64) NULL,
	summary varchar(1024) NULL
);