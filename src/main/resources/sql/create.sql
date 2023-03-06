CREATE DATABASE product_ean;

CREATE TABLE product_ean.destination (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(52) DEFAULT 'Not set',
  code VARCHAR(1) NOT NULL,
  CONSTRAINT uniqueCodeConstraint UNIQUE (code)
);

CREATE TABLE product_ean.provider (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(52) DEFAULT 'Not set',
  code VARCHAR(7) NOT NULL,
  CONSTRAINT uniqueCodeConstraint UNIQUE (code)
);

CREATE TABLE product_ean.product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  ean_code VARCHAR(13) NOT NULL,
  product_code VARCHAR(5) NOT NULL,
  destination_id INT NOT NULL,
  provider_id INT NOT NULL,
  CONSTRAINT uniqueEanConstraint UNIQUE (product_code, destination_id, provider_id),
  FOREIGN KEY (destination_id) REFERENCES destination (id),
  FOREIGN KEY (provider_id) REFERENCES provider (id)
);