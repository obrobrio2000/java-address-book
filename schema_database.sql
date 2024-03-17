CREATE DATABASE IF NOT EXISTS rubrica;

USE rubrica;

CREATE TABLE persone (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    eta INT NOT NULL
);
