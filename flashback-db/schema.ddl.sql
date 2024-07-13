DROP TABLE IF EXISTS t_direction;
DROP TABLE IF EXISTS t_directors;
DROP TABLE IF EXISTS t_favorites;
DROP TABLE IF EXISTS t_participants;
DROP TABLE IF EXISTS t_program;
DROP TABLE IF EXISTS t_movies;
DROP TABLE IF EXISTS t_genres;
DROP TABLE IF EXISTS t_retrospectives;
DROP TABLE IF EXISTS t_devices;
DROP TABLE IF EXISTS t_users;
DROP TABLE IF EXISTS t_roles;

SET client_encoding to 'UTF8';

CREATE TABLE t_genres (
	id SERIAL,
	genre_name VARCHAR(40) NOT NULL UNIQUE,
	CONSTRAINT pk_genre_id
		PRIMARY KEY(id)
);

CREATE TABLE t_movies (
	id SERIAL,
	isan VARCHAR(40) NOT NULL UNIQUE,
	title VARCHAR(200) NOT NULL,
	genre_id INTEGER,
	release_year INTEGER NOT NULL,
	poster VARCHAR(300) NOT NULL,
	trailer VARCHAR(300),
	summary VARCHAR(1000) NOT NULL,
	CONSTRAINT pk_movie_id
		PRIMARY KEY(id),
	CONSTRAINT fk_genre_id
        FOREIGN KEY(genre_id)
        REFERENCES t_genres(id)
);

CREATE TABLE t_directors (
	id SERIAL,
	firstname VARCHAR(40) NOT NULL,
	lastname VARCHAR(40) NOT NULL,
	director_code VARCHAR(100) NOT NULL UNIQUE,
	CONSTRAINT pk_director_id
		PRIMARY KEY(id)
);

CREATE TABLE t_direction ( 
	id SERIAL,
	movie_id INTEGER,
	director_id INTEGER,
	UNIQUE (movie_id, director_id),
	CONSTRAINT pk_direction_id
		PRIMARY KEY(id),
	CONSTRAINT fk_movie_id
		FOREIGN KEY(movie_id)
		REFERENCES t_movies(id),
	CONSTRAINT fk_director_id
		FOREIGN KEY(director_id)
		REFERENCES t_directors(id)
);

CREATE TABLE t_roles (
	id SERIAL,
	role_name VARCHAR(5) NOT NULL UNIQUE,
	CONSTRAINT pk_role_id
		PRIMARY KEY(id)
);

CREATE TABLE t_users (
	id SERIAL,
	firstname VARCHAR(50) NOT NULL,
	lastname VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL UNIQUE,
	user_password VARCHAR(60) NOT NULL,
	role_id INTEGER,
	created_at TIMESTAMP NOT NULL,
	CONSTRAINT pk_user_id
		PRIMARY KEY(id),
	CONSTRAINT fk_role_id
        FOREIGN KEY(role_id)
        REFERENCES t_roles(id)
);

CREATE TABLE t_favorites(
	id SERIAL,
	movie_id INTEGER,
	user_id INTEGER,
	UNIQUE (movie_id, user_id),
	CONSTRAINT pk_faorite_id
		PRIMARY KEY(id),
	CONSTRAINT fk_movie_id
		FOREIGN KEY(movie_id)
		REFERENCES t_movies(id),
	CONSTRAINT fk_user_id
		FOREIGN KEY(user_id)
		REFERENCES t_users(id)
);

CREATE TABLE t_devices (
	id SERIAL,
	device_name VARCHAR(40) NOT NULL UNIQUE,
	CONSTRAINT pk_device_id
		PRIMARY KEY(id)
);

CREATE TABLE t_retrospectives(
	id SERIAL,
	retrospective_name VARCHAR(100) NOT NULL UNIQUE,
	start_date TIMESTAMP NOT NULL,
	end_date TIMESTAMP NOT NULL,
	description VARCHAR(1000) NOT NULL,
	device_id INTEGER,
	user_id INTEGER,
	movies_number INT NOT NULL,
	participants_number INT NOT NULL,
	CONSTRAINT pk_retrospective_id
		PRIMARY KEY(id),
	CONSTRAINT fk_device_id
        FOREIGN KEY(device_id)
        REFERENCES t_devices(id),
    CONSTRAINT fk_user_id
    	FOREIGN KEY(user_id)
    	REFERENCES t_users(id)
);

CREATE TABLE t_program ( 
	id SERIAL,
	movie_id INTEGER,
	retrospective_id INTEGER,
	UNIQUE (movie_id, retrospective_id),
	CONSTRAINT pk_program_id
		PRIMARY KEY(id),
	CONSTRAINT fk_movie_id
		FOREIGN KEY(movie_id)
		REFERENCES t_movies(id),
	CONSTRAINT fk_retrospective_id
		FOREIGN KEY(retrospective_id)
		REFERENCES t_retrospectives(id)
);

CREATE TABLE t_participants ( 
	id SERIAL,
	user_id INTEGER,
	retrospective_id INTEGER,
	UNIQUE (user_id, retrospective_id),
	CONSTRAINT pk_participants_id
		PRIMARY KEY(id),
	CONSTRAINT fk_user_id
		FOREIGN KEY(user_id)
		REFERENCES t_users(id),
	CONSTRAINT fk_retrospective_id
		FOREIGN KEY(retrospective_id)
		REFERENCES t_retrospectives(id)
);

/*Stored procedures to copy data in movies, direction and directors tables*/
CREATE OR REPLACE PROCEDURE copy_data()
LANGUAGE plpgsql
AS $procedure$
	DECLARE 
		r RECORD;		
	BEGIN
		TRUNCATE TABLE t_movies,t_directors,t_direction, t_favorites, t_program;
		FOR r IN 
			SELECT * FROM movies_data
		LOOP
			/*if trailer is null, DB will insert "null" into movies table to avoid empty values.*/
			IF r.trailer IS NULL THEN
				INSERT INTO t_movies(isan, title, genre_id, release_year, poster, summary)
				VALUES (r.isan, r.title,r.genre_id, r.release_year, r.poster, r.summary);
			ELSE
				INSERT INTO t_movies(isan, title, genre_id, release_year, poster, trailer, summary)
				VALUES (r.isan, r.title,r.genre_id, r.release_year, r.poster, r.trailer, r.summary);
			END IF;
		END LOOP;
	
		FOR r IN 
			SELECT * FROM directors_data
		LOOP
			INSERT INTO t_directors(firstname, lastname, director_code)
			VALUES (r.firstname, r.lastname, r.director_code);
		END LOOP;
		
		FOR r IN 
			SELECT * FROM direction_data
		LOOP
			INSERT INTO t_direction(movie_id, director_id)
			VALUES (r.movie_id, r.director_id);
		END LOOP;
	END
$procedure$;