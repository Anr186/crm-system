CREATE TABLE IF NOT EXISTS sellers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_info TEXT,
    registration_date TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    seller_id INTEGER REFERENCES sellers(id),
    amount DECIMAL(19, 2) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP NOT NULL
);