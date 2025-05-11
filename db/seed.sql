USE inventario;

INSERT INTO Repuesto (sku, nombre, descripcion, precio, stock) VALUES
(1001, 'Filtro de aceite', 'Filtro de aceite para motor', 15.50, 100),
(1002, 'Bujía estándar', 'Bujía para motores gasolina', 8.25, 200),
(1003, 'Correa de distribución', 'Correa resistente nylon', 45.00, 50);

INSERT INTO Reserva (sku, cantidad) VALUES
(1001, 2),
(1002, 1);