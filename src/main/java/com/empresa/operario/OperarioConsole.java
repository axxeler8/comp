package com.empresa.operario;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;
import com.empresa.inventario.Ubicacion;
import com.empresa.inventario.Vehiculo;

import java.rmi.Naming;
import java.util.Scanner;

public class OperarioConsole {
    public static void main(String[] args) throws Exception {
        InventarioService svc = (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Operario ---");
            System.out.println("1) Agregar repuesto");
            System.out.println("2) Liberar repuesto");
            System.out.println("3) Agregar reserva");
            System.out.println("4) Liberar reserva");
            System.out.println("5) Salir");

            int op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1:
                    agregarRepuesto(svc, sc);
                    break;
                case 2:
                    liberarRepuesto(svc, sc);
                    break;
                case 3:
                    agregarReserva(svc, sc);
                    break;
                case 4:
                    liberarReserva(svc, sc);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void agregarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Repuesto --");
        int idUb;
        while (true) {
            System.out.print("ID de Ubicación: ");
            idUb = Integer.parseInt(sc.nextLine());
            if (svc.consultarUbicacion(idUb) == null) {
                System.out.println("⚠ Ubicación inexistente. Intenta de nuevo.");
            } else {
                break;
            }
        }
        Ubicacion ub = svc.consultarUbicacion(idUb);

        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            if (svc.consultarRepuesto(sku) != null) {
                System.out.println("⚠ SKU ya existe. Intenta otro.");
            } else {
                break;
            }
        }

        int cantidad;
        while (true) {
            System.out.print("Cantidad: ");
            cantidad = Integer.parseInt(sc.nextLine());
            int stockActual = svc.consultarStockUbicacion(idUb);
            if (stockActual + cantidad > ub.getCapacidad()) {
                System.out.printf("⚠ Excede capacidad. Actual: %d, Máxima: %d. Intenta de nuevo.%n", stockActual, ub.getCapacidad());
            } else {
                break;
            }
        }

        System.out.print("Precio: ");
        int precio = Integer.parseInt(sc.nextLine());

        System.out.print("Categoría (texto): ");
        String categoria = sc.nextLine().trim();

        boolean disponible;
        while (true) {
            System.out.print("Disponible (1=si / 0=no): ");
            String in = sc.nextLine().trim();
            if ("1".equals(in)) {
                disponible = true;
                break;
            } else if ("0".equals(in)) {
                disponible = false;
                break;
            } else {
                System.out.println("⚠ Responde 1 o 0. Intenta de nuevo.");
            }
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        svc.agregarRepuesto(idUb, sku, cantidad, precio, categoria, disponible, nombre);
        System.out.println("✔ Repuesto agregado.");
    }

    private static void liberarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Repuesto --");

        // Leer ubicación
        System.out.print("ID de Ubicación: ");
        int idUb = Integer.parseInt(sc.nextLine());

        // Validar SKU en ubicación
        Repuesto rep;
        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            rep = svc.consultarRepuestoEnUbicacion(idUb, sku);
            if (rep == null) {
                System.out.println("⚠ No existe el SKU " + sku + " en la ubicación " + idUb + ". Intenta de nuevo.");
            } else {
                break;
            }
        }

        // Validar cantidad a liberar
        while (true) {
            System.out.print("Cantidad a liberar (máx " + rep.getCantidad() + "): ");
            int cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad < 1 || cantidad > rep.getCantidad()) {
                System.out.println("⚠ Cantidad inválida. Debe estar entre 1 y " + rep.getCantidad() + ". Intenta de nuevo.");
            } else {
                svc.liberarRepuesto(idUb, sku, cantidad);
                System.out.println("✔ Repuesto liberado.");
                break;
            }
        }
    }

    private static void agregarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Reserva --");

        // Leer ID de Vehículo y validar existencia
        int idVeh;
        while (true) {
            System.out.print("ID de Vehículo: ");
            idVeh = Integer.parseInt(sc.nextLine());
            Vehiculo veh = svc.consultarVehiculo(idVeh);
            if (veh == null) {
                System.out.println("⚠ Vehículo no encontrado. Intenta de nuevo.");
            } else {
                break;
            }
        }

        // Validar SKU global
        Repuesto rep;
        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            rep = svc.consultarRepuesto(sku);
            if (rep == null) {
                System.out.println("⚠ No existe el SKU " + sku + ". Intenta de nuevo.");
            } else if (rep.getCantidad() < 1) {
                System.out.println("⚠ No hay stock disponible para el SKU " + sku + ".");
                return;
            } else {
                break;
            }
        }

        // Validar cantidad a reservar
        int cantidad;
        while (true) {
            System.out.print("Cantidad a reservar (máx " + rep.getCantidad() + "): ");
            cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad < 1 || cantidad > rep.getCantidad()) {
                System.out.println("⚠ Cantidad inválida. Debe estar entre 1 y " + rep.getCantidad() + ". Intenta de nuevo.");
            } else {
                break;
            }
        }

        // Registrar reserva; Database.insertarReserva descontará stock
        svc.agregarReserva(idVeh, sku, cantidad);
        System.out.println("✔ Reserva creada.");
    }

    private static void liberarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Reserva --");
        System.out.print("ID de Reserva: ");
        int idRes = Integer.parseInt(sc.nextLine());

        svc.liberarReserva(idRes);
        System.out.println("✔ Reserva liberada.");
    }
}
