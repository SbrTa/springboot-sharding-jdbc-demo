DROP DATABASE IF EXISTS db_bd;
DROP DATABASE IF EXISTS db_us;
CREATE DATABASE db_bd;
CREATE DATABASE db_us;

DROP TABLE IF EXISTS db_bd.payment_bdt_2022_08;
CREATE TABLE db_bd.payment_bdt_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_bd.payment_bdt_2022_09;
CREATE TABLE db_bd.payment_bdt_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_bd.payment_usd_2022_08;
CREATE TABLE db_bd.payment_usd_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_bd.payment_usd_2022_09;
CREATE TABLE db_bd.payment_usd_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_us.payment_bdt_2022_08;
CREATE TABLE db_us.payment_bdt_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_us.payment_bdt_2022_09;
CREATE TABLE db_us.payment_bdt_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_us.payment_usd_2022_08;
CREATE TABLE db_us.payment_usd_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_us.payment_usd_2022_09;
CREATE TABLE db_us.payment_usd_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  region VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);