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