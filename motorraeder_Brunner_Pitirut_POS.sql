--PostgreSQL Script
\c template1
DROP DATABASE motorraeder;
CREATE DATABASE motorraeder;
\c motorraeder;

--DROP TABLE raeder CASCADE;
CREATE TABLE raeder (
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	date BIGINT,
	PRIMARY KEY (id)
);

CREATE TABLE deletedEntry(
	id INTEGER,
	marke VARCHAR(255),
	modell VARCHAR(255),
	leistung BIGINT,
	date BIGINT,
	PRIMARY KEY (id)
);

-- Trigger
CREATE OR REPLACE FUNCTION updateTimestamp()
	RETURNS TRIGGER AS $$
	BEGIN
	   NEW.date = extract('epoch' from now())::bigint;
	   RETURN NEW;
	END;
	$$ language 'plpgsql';


CREATE TRIGGER updateTimestamp BEFORE UPDATE ON raeder FOR EACH ROW
EXECUTE PROCEDURE updateTimestamp();

CREATE OR REPLACE FUNCTION deletedEntry()
	RETURNS TRIGGER AS $$
	BEGIN
		IF (TG_OP = 'DELETE') THEN
		INSERT INTO deletedEntry(id, marke, modell, leistung, date) VALUES (old.id, old.marke, old.modell, old.leistung, old.date);
		RETURN OLD;
		END IF;
	END;
	$$ language 'plpgsql';


CREATE TRIGGER deletedEntry AFTER DELETE ON raeder FOR EACH ROW
EXECUTE PROCEDURE deletedEntry();

--Inserts
INSERT INTO raeder VALUES (1, 'Honda', 'CBR-600',120, extract('epoch' from now()));
INSERT INTO raeder VALUES (2, 'Honda', 'CBR-1000',180, extract('epoch' from now()));
INSERT INTO raeder VALUES (3, 'Yamaha', 'R6',130, extract('epoch' from now()));
INSERT INTO raeder VALUES (4, 'Yamaha', 'R1',190, extract('epoch' from now()));
INSERT INTO raeder VALUES (5, 'Kawasaki', 'ZX-10R',200, extract('epoch' from now()));
INSERT INTO raeder VALUES (6, 'BMW', 'S1000RR',200, extract('epoch' from now()));
INSERT INTO raeder VALUES (7, 'Suzuki', 'GSR 600',101, extract('epoch' from now()));