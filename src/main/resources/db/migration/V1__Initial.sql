CREATE TABLE Stock (
    ProductCode VARCHAR(255) UNIQUE,
    ProductName VARCHAR(255) NOT NULL,
    Store VARCHAR(255) NOT NULL,
    Reserved BOOLEAN DEFAULT 0 NOT NULL
);