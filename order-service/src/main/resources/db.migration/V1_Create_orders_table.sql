CREATE TABLE IF NOT EXISTS orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name VARCHAR(255) NOT NULL,
    vehicle_type TEXT NOT NULL,
    ride_distance TEXT NOT NULL,
    total_amount DECIMAL (10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DTAETIME
);
