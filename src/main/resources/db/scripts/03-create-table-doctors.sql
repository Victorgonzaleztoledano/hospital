CREATE TABLE doctors (
    dni CHAR(9) NOT NULL,
    address VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    code CHAR(36) NOT NULL,
    working_time TIME NOT NULL,
    end_working_time TIME NOT NULL,
    experience INT CHECK (experience >= 0 AND experience <= 50),
    PRIMARY KEY (dni)
);