CREATE DATABASE IF NOT EXISTS userdao
CHARACTER SET utf8
COLLATE utf8_unicode_ci;

USE userdao;

CREATE TABLE users(
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);