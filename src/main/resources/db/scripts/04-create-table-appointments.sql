CREATE TABLE appointments (
    id CHAR(36) NOT NULL,
    date_appointment DATE NOT NULL,
    time_appointment TIME NOT NULL,
    dni_patient CHAR(9) NOT NULL,
    employee_code CHAR(36) NOT NULL,
    PRIMARY KEY (id)
);


