DROP DATABASE IF EXISTS db_sh;
DROP DATABASE IF EXISTS db_sz;
CREATE DATABASE db_sh;
CREATE DATABASE db_sz;

DROP TABLE IF EXISTS db_sh.payment_bdt_2022_08;
CREATE TABLE db_sh.payment_bdt_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sh.payment_bdt_2022_09;
CREATE TABLE db_sh.payment_bdt_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sh.payment_usd_2022_08;
CREATE TABLE db_sh.payment_usd_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sh.payment_usd_2022_09;
CREATE TABLE db_sh.payment_usd_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sz.payment_bdt_2022_08;
CREATE TABLE db_sz.payment_bdt_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sz.payment_bdt_2022_09;
CREATE TABLE db_sz.payment_bdt_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sz.payment_usd_2022_08;
CREATE TABLE db_sz.payment_usd_2022_08
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);
DROP TABLE IF EXISTS db_sz.payment_usd_2022_09;
CREATE TABLE db_sz.payment_usd_2022_09
(
  id BIGINT(20) PRIMARY KEY NOT NULL,
  currency VARCHAR(3),
  exchange VARCHAR(10),
  ask INT(11),
  bid INT(11),
  time DATETIME
);