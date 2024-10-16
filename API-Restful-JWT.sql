#CREATE DATABASE api_restful_jwt;

USE api_restful_jwt;

DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS video_games_sales;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS video_games;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS classifications;

#ROLES
CREATE TABLE roles(
	id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    
    CONSTRAINT UQ_NAME UNIQUE (name),
    CONSTRAINT PRIMARY KEY (id)
);

INSERT INTO roles(name) VALUES ('ROLE_ADMIN'),('ROLE_USER');

#USUARIOS
CREATE TABLE users(
	id INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password TEXT NOT NULL,
    enable TINYINT NOT NULL DEFAULT 1,
    
    CONSTRAINT PRIMARY KEY (id),
	CONSTRAINT UQ_USERNAME UNIQUE (username)
);

#USERS_ROLES
CREATE TABLE users_roles(
	user_id INT NOT NULL,
    role_id INT NOT NULL,
    
    CONSTRAINT PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT FK_roles FOREIGN KEY (role_id) REFERENCES roles(id)
	ON DELETE CASCADE
    ON UPDATE CASCADE
);

#GENEROS
CREATE TABLE genres(
	id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    
    CONSTRAINT PRIMARY KEY (id)
);

#CLASIFICACIONES
CREATE TABLE classifications(
	id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    
    CONSTRAINT PRIMARY KEY (id)
);

#VENTAS
CREATE TABLE sales(
	id INT AUTO_INCREMENT,
    code VARCHAR(200) NOT NULL,
    value INT NOT NULL,
    date DATE NOT NULL,
    
    CONSTRAINT PRIMARY KEY (id)
);

#VIDEOJUEGOS
CREATE TABLE video_games(	
	id INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price INT NOT NULL,
    id_genres INT NOT NULL,
    id_classifications INT NOT NULL,
    
    CONSTRAINT PRIMARY KEY (id),
	CONSTRAINT FK_video_games_genres FOREIGN KEY (id_genres) REFERENCES genres(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
	CONSTRAINT FK_video_games_classifications FOREIGN KEY (id_classifications) REFERENCES classifications(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

#VENTAS_VIDEOJUEGOS
CREATE TABLE video_games_sales(
	id INT AUTO_INCREMENT,
    id_video_games INT NOT NULL,
    id_sales INT NOT NULL,
    
    CONSTRAINT PRIMARY KEY (id),
	CONSTRAINT FK_u_video_games FOREIGN KEY (id_video_games) REFERENCES video_games(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
	CONSTRAINT FK_u_sales FOREIGN KEY (id_sales) REFERENCES sales(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

#CONSULTAS

-- SELECT * FROM users;
-- SELECT * FROM roles;
-- SELECT * FROM users_roles;
-- SELECT * FROM genres;
-- SELECT * FROM classifications;
-- SELECT * FROM sales;
-- SELECT * FROM video_games;
-- SELECT * FROM video_games_sales;
