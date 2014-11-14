--PostgreSQL Script
\c template1
DROP DATABASE motorraederv2;
CREATE DATABASE motorraederv2;
\c motorraederv2;

--DROP TABLE raeder CASCADE;
CREATE TABLE raeder (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default '0',
	PRIMARY KEY (id)
);

CREATE TABLE deletedEntry(
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default '0',
	PRIMARY KEY (id)
);

CREATE TABLE insertEntry (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	version INT,
	wreifen boolean not null default '0',
	PRIMARY KEY (id)
);

-- Trigger
CREATE OR REPLACE FUNCTION updateVersion()
	RETURNS TRIGGER AS $$
	BEGIN
	   NEW.version = OLD.version+1;
	   RETURN NEW;
	END;
	$$ language 'plpgsql';


CREATE TRIGGER updateVersion BEFORE UPDATE ON raeder FOR EACH ROW
EXECUTE PROCEDURE updateVersion();

CREATE OR REPLACE FUNCTION deletedEntry()
	RETURNS TRIGGER AS $$
	BEGIN
		IF (TG_OP = 'DELETE') THEN
		INSERT INTO deletedentry(id, marke, modell, leistung, version, wreifen) VALUES (old.id, old.marke, old.modell, old.leistung, old.version, old.wreifen);
		RETURN OLD;
		END IF;
	END;
	$$ language 'plpgsql';


CREATE TRIGGER deletedEntry AFTER DELETE ON raeder FOR EACH ROW
EXECUTE PROCEDURE deletedEntry();


CREATE OR REPLACE FUNCTION insertEntry()
	RETURNS TRIGGER AS $$
	BEGIN
		IF (TG_OP = 'INSERT') THEN
		INSERT INTO insertEntry(id, marke, modell, leistung, version, wreifen) VALUES (new.id, new.marke, new.modell, new.leistung, new.version, new.wreifen);
		RETURN NEW;
		END IF;
	END;
	$$ language 'plpgsql';


CREATE TRIGGER insertEntry AFTER INSERT ON raeder FOR EACH ROW
EXECUTE PROCEDURE insertEntry();

--Inserts
INSERT INTO raeder VALUES (1, 'Honda', 'CBR-600',120, 0, '1');
INSERT INTO raeder VALUES (2, 'Honda', 'CBR-1000',180, 0, '1');
INSERT INTO raeder VALUES (3, 'Yamaha', 'R6',130, 0, '1');
INSERT INTO raeder VALUES (4, 'Yamaha', 'R1',190, 0, '0');
INSERT INTO raeder VALUES (5, 'Kawasaki', 'ZX-10R',200, 0, '1');
INSERT INTO raeder VALUES (6, 'BMW', 'S1000RR',200, 0, '0');
INSERT INTO raeder VALUES (7, 'Suzuki', 'GSR 600',110, 0, '0');
INSERT INTO raeder VALUES (8, 'Kawasaki', 'Ninja', 500, 0, '1');
INSERT INTO raeder VALUES (9, 'BMW', 'SL1200', 180, 0, '1');
INSERT INTO raeder VALUES (10, 'Suzuki', 'GSR 800', 140, 0, '0'); 
