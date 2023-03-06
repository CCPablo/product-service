
INSERT INTO destination (id, name, code)
VALUES (1, 'España', '1'),
       (2, 'España', '2'),
       (3, 'España', '3'),
       (4, 'España', '4'),
       (5, 'España', '5'),
       (6, 'Portugal', '6'),
       (7, 'Almacenes', '8'),
       (8, 'Oficinas', '9'),
       (9, 'Colmenas', '0');

INSERT INTO provider (id, name, code)
VALUES (1, 'Hacendado', '8437008');

INSERT INTO product (id, ean_code, product_code, destination_id, provider_id)
VALUES (1, '8437008459051', '45905', 1, 1),
       (2, '2345678901234', '90123', 2, 1);