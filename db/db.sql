-- -----------------------------------------------------
-- 1) Eliminar y recrear la base de datos
-- -----------------------------------------------------
DROP DATABASE IF EXISTS `bd_lyl`;
CREATE DATABASE `bd_lyl` DEFAULT CHARACTER SET utf8;
USE `bd_lyl`;

-- -----------------------------------------------------
-- 2) Ajustes temporales para crear tablas en cualquier orden
-- -----------------------------------------------------
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, 
    SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- 3) Tablas
-- -----------------------------------------------------

-- Tabla: ubicaciones
CREATE TABLE `ubicaciones` (
  `idUbicacion` INT NOT NULL AUTO_INCREMENT,
  `nombre`       VARCHAR(45) NOT NULL,
  `direccion`    VARCHAR(45) NOT NULL,
  `capacidad`    INT NULL,
  PRIMARY KEY (`idUbicacion`),
  UNIQUE KEY `uq_ubicaciones_id` (`idUbicacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabla: repuestos
CREATE TABLE `repuestos` (
  `idRepuesto`   INT NOT NULL AUTO_INCREMENT,
  `idUbicacion`  INT NOT NULL,
  `sku`          INT NOT NULL,
  `cantidad`     INT NOT NULL,
  `precio`       INT NOT NULL,
  `categoria`    INT NULL,
  `disponible`   ENUM('si','no') NOT NULL,
  `nombre`       VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRepuesto`),
  UNIQUE KEY `uq_repuestos_id` (`idRepuesto`),
  UNIQUE KEY `uq_repuestos_sku` (`sku`),
  KEY `idx_repuestos_ubicacion` (`idUbicacion`),
  CONSTRAINT `fk_repuestos_ubicaciones`
    FOREIGN KEY (`idUbicacion`)
    REFERENCES `ubicaciones` (`idUbicacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabla: vehiculos
CREATE TABLE `vehiculos` (
  `idVehiculo`   INT NOT NULL AUTO_INCREMENT,
  `anio`         INT NULL,
  `idUbicacion`  INT NOT NULL,
  `nombre`       VARCHAR(45) NOT NULL,
  `modelo`       VARCHAR(45) NOT NULL,
  `cilindraje`   VARCHAR(45) NULL,
  `color`        VARCHAR(45) NULL,
  PRIMARY KEY (`idVehiculo`),
  UNIQUE KEY `uq_vehiculos_id` (`idVehiculo`),
  KEY `idx_vehiculos_ubicacion` (`idUbicacion`),
  CONSTRAINT `fk_vehiculos_ubicaciones`
    FOREIGN KEY (`idUbicacion`)
    REFERENCES `ubicaciones` (`idUbicacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Tabla: reservas
CREATE TABLE `reservas` (
  `idReserva`    INT NOT NULL AUTO_INCREMENT,
  `idVehiculo`   INT NOT NULL,
  `sku`          INT NOT NULL,
  `cantidad`     INT NOT NULL,
  `fecha`        DATETIME NULL,
  PRIMARY KEY (`idReserva`),
  UNIQUE KEY `uq_reservas_id` (`idReserva`),
  KEY `idx_reservas_vehiculo` (`idVehiculo`),
  KEY `idx_reservas_sku` (`sku`),
  CONSTRAINT `fk_reservas_vehiculos`
    FOREIGN KEY (`idVehiculo`)
    REFERENCES `vehiculos` (`idVehiculo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservas_repuestos`
    FOREIGN KEY (`sku`)
    REFERENCES `repuestos` (`sku`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- 4) Restaurar modos y checks originales
-- -----------------------------------------------------
SET SQL_MODE            = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS  = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS       = @OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- 5) Insertar datos de ejemplo
-- -----------------------------------------------------
USE `bd_lyl`;

-- 5.1 Insertar ubicaciones
INSERT INTO ubicaciones (nombre, direccion, capacidad) VALUES
  ('Taller Central', 'Calle Motor 123', 500),
  ('Bodega Norte',   'Avenida Partes 456', 2000),
  ('Sucursal Sur',   'Bulevar Repuestos 789', 1500);

-- 5.2 Insertar repuestos
INSERT INTO repuestos (idUbicacion, sku, cantidad, precio, categoria, disponible, nombre) VALUES
  (1, 1001, 50,  15, 1, 'si', 'Filtro de aire'),
  (1, 1002, 30,   8, 1, 'si', 'Bujías'),
  (2, 1003, 20,  45, 2, 'no', 'Pastillas de freno'),
  (3, 1004, 10, 120, 3, 'si', 'Amortiguador');

-- 5.3 Insertar vehículos
INSERT INTO vehiculos (anio, idUbicacion, nombre, modelo, cilindraje, color) VALUES
  (2020, 1, 'Toyota Corolla', 'LE', '1.8L', 'Blanco'),
  (2019, 2, 'Honda Civic',    'EX', '2.0L', 'Rojo');

-- 5.4 Insertar reservas
INSERT INTO reservas (idVehiculo, sku, cantidad, fecha) VALUES
  (1, 1001, 2, NOW()),
  (2, 1003, 1, NOW());
