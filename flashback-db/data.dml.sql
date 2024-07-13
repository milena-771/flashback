DELETE FROM t_genres;
DELETE FROM t_roles;
DELETE FROM t_users;
DELETE FROM t_favorites;
DELETE FROM t_program;
DELETE FROM t_devices;
DELETE FROM t_retrospectives;

SET client_encoding to 'UTF8';

INSERT INTO t_genres (genre_name)
	VALUES ('Drame'),('Action'),('Aventure'),('Biopic'),('Burlesque'),('Catastrophe'),
		('Comédie'),('Policier'),('Documentaire'),('Espionnage'),('Fantastique'),
		('Fantasy'),('Gangsters'),('Guerre'),('Historique'),('Horreur'),('Musical'),('Road-movie'),('Romance'),
		('Science-fiction'),('Super-héros'),('Thriller'),('Western'),('Animation');

INSERT INTO t_roles (role_name)
	VALUES ('ADMIN'),('USER');

INSERT INTO t_devices (device_name)
	VALUES ('Cinéma'),('Plateforme en ligne'),('DVD'),('Cinémathèque'),('Télévision');
	
CALL copy_data();