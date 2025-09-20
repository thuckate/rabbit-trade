CREATE DATABASE IF NOT EXISTS vietlott
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS power655 (
    type VARCHAR(10) NOT NULL,
    date VARCHAR(14) NOT NULL,
    drawId VARCHAR(10) NOT NULL,
    draw VARCHAR(100),
    PRIMARY KEY (type, date, drawId)
);

CREATE TABLE IF NOT EXISTS mega645 (
    type VARCHAR(10) NOT NULL,
    date VARCHAR(14) NOT NULL,
    drawId VARCHAR(10) NOT NULL,
    draw VARCHAR(100),
    PRIMARY KEY (type, date, drawId)
);

CREATE TABLE IF NOT EXISTS lotto535 (
    type VARCHAR(10) NOT NULL,
    date VARCHAR(14) NOT NULL,
    drawId VARCHAR(10) NOT NULL,
    draw VARCHAR(100),
    PRIMARY KEY (type, date, drawId)
);