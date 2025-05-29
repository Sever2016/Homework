CREATE TABLE people (
    ID INTEGER PRIMARY KEY,
    cars_id TEXT REFERENCES cars (id),
    name TEXT,
    age SMALLSERIAL,
    driver_licience BOOLEAN
);
CREATE TABLE cars (
    ID INTEGER,
    brand TEXT,
    model TEXT PRIMARY KEY,
    price REAL
);