CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id_user SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    identity_document VARCHAR(50) NULL,
    phone VARCHAR(10) NULL,
    role_id INTEGER NULL REFERENCES roles(id),
    base_salary DOUBLE PRECISION NOT NULL CHECK (base_salary >= 0 AND base_salary <= 15000000),
    date_of_birth DATE NULL,
    address VARCHAR(255) NULL
);
