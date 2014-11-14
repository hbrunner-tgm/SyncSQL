--MySQL DATABASE

DROP DATABASE IF EXISTS motorraederv2;
CREATE DATABASE motorraederv2;
USE motorraederv2;

--DROP TABLE raeder CASCADE;
CREATE TABLE raeder (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default 0,
	PRIMARY KEY (id)
)ENGINE=INNODB;

CREATE TABLE deletedEntry (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default 0,
	PRIMARY KEY (id)
)ENGINE=INNODB;

CREATE TABLE insertEntry (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default 0,
	PRIMARY KEY (id)
)ENGINE=INNODB;


INSERT INTO raeder VALUES (1, 'Honda', 'CBR-600',120, 0, 1);
INSERT INTO raeder VALUES (2, 'Honda', 'CBR-1000',180, 0, 1);
INSERT INTO raeder VALUES (3, 'Yamaha', 'R6',130, 0, 1);
INSERT INTO raeder VALUES (4, 'Yamaha', 'R1',190, 0, 0);
INSERT INTO raeder VALUES (5, 'Kawasaki', 'ZX-10R',200, 0, 1);
INSERT INTO raeder VALUES (6, 'BMW', 'S1000RR',200, 0, 0);
INSERT INTO raeder VALUES (7, 'Suzuki', 'GSR 600',110, 0, 0);
INSERT INTO raeder VALUES (8, 'Kawasaki', 'Ninja', 500, 0, 1);
INSERT INTO raeder VALUES (9, 'BMW', 'SL1200', 180, 0, 1);
INSERT INTO raeder VALUES (10, 'Suzuki', 'GSR 800', 140, 0, 0); 





DELIMITER $$
CREATE TRIGGER updateVersion BEFORE UPDATE ON raeder
FOR EACH ROW
BEGIN
	SET NEW.version = OLD.version+1;
END;
$$ DELIMITER;


DELIMITER $$
CREATE TRIGGER deletedEntry BEFORE DELETE ON raeder
FOR EACH ROW
BEGIN
	INSERT INTO deletedEntry SELECT * FROM raeder WHERE id= old.id;
END;
$$ DELIMITER;


DELIMITER $$
CREATE TRIGGER insertEntry AFTER INSERT ON raeder
FOR EACH ROW
BEGIN
	INSERT INTO insertEntry SELECT * FROM raeder WHERE id= new.id;
END;
$$ DELIMITER;

UPDATE raeder SET marke='AVG' WHERE marke='Ford';
