// **OperarioConsole.java**
package com.empresa.operario;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Ubicacion;
import com.empresa.inventario.Repuesto;

import java.rmi.Naming;
import java.util.InputMismatchException;
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

            int op = readInt(sc, "Seleccione una opción: ", 1, 5);
            switch (op) {
                case 1: agregarRepuesto(svc, sc); break;
                case 2: liberarRepuesto(svc, sc); break;
                case 3: agregarReserva(svc, sc); break;
                case 4: liberarReserva(svc, sc); break;
                case 5:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);
            }
        }
    }

    private static void agregarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Repuesto --");
        int idUb;
        // Validar ID de ubicación
        while (true) {
            idUb = readInt(sc, "Ingrese ID de Ubicación: ", 1, Integer.MAX_VALUE);
            if (svc.consultarUbicacion(idUb) == null) {
                System.out.println("⚠ Ubicación inexistente.");
            } else break;
        }
        Ubicacion ub = svc.consultarUbicacion(idUb);

        int sku;
        // Validar SKU existente
        while (true) {
            sku = readInt(sc, "Ingrese SKU: ", 1, Integer.MAX_VALUE);
            if (svc.consultarRepuesto(sku) != null) {
                System.out.println("⚠ SKU ya existe.");
            } else break;
        }

        int cantidad;
        // Validar cantidad y capacidad
        while (true) {
            cantidad = readInt(sc, "Ingrese Cantidad: ", 1, Integer.MAX_VALUE);
            int stockActual = svc.consultarStockUbicacion(idUb);
            if (stockActual + cantidad > ub.getCapacidad()) {
                System.out.printf("⚠ Excede capacidad. Actual: %d, Máxima: %d.%n", stockActual, ub.getCapacidad());
            } else break;
        }

        int precio = readInt(sc, "Ingrese Precio: ", 0, Integer.MAX_VALUE);

        boolean disponible;
        // Validar boolean
        while (true) {
            String input;
            System.out.print("¿Disponible? (si/no): ");
            input = sc.nextLine().trim().toLowerCase();
            if ("si".equals(input)) { disponible = true; break; }
            if ("no".equals(input)) { disponible = false; break; }
            System.out.println("Entrada no válida: responda 'si' o 'no'.");
        }

        String nombre;
        // Validar nombre no vacío
        while (true) {
            System.out.print("Ingrese Nombre del Repuesto: ");
            nombre = sc.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("⚠ Nombre vacío.");
            } else break;
        }

        svc.agregarRepuesto(idUb, sku, cantidad, precio, disponible, nombre);
        System.out.println("✔ Repuesto agregado correctamente.");
    }

    private static void liberarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Repuesto --");
        int idUb;
        while (true) {
            idUb = readInt(sc, "Ingrese ID de Ubicación: ", 1, Integer.MAX_VALUE);
            if (svc.consultarUbicacion(idUb) == null) {
                System.out.println("⚠ Ubicación inexistente.");
            } else break;
        }

        int sku;
        Repuesto r;
        while (true) {
            sku = readInt(sc, "Ingrese SKU: ", 1, Integer.MAX_VALUE);
            r = svc.consultarRepuestoEnUbicacion(idUb, sku);
            if (r == null) {
                System.out.println("⚠ No existe repuesto con SKU " + sku + " en la ubicación " + idUb + ".");
            } else break;
        }

        int cant;
        while (true) {
            cant = readInt(sc, String.format("Ingrese Cantidad a liberar (hasta %d): ", r.getCantidad()), 1, r.getCantidad());
            break;
        }

        svc.liberarRepuesto(idUb, sku, cant);
        System.out.println("✔ Repuesto liberado correctamente.");
    }

    private static void agregarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Reserva --");
        int idVeh;
        while (true) {
            idVeh = readInt(sc, "Ingrese ID de Vehículo: ", 1, Integer.MAX_VALUE);
            if (svc.consultarVehiculo(idVeh) == null) System.out.println("⚠ Vehículo inexistente.");
            else break;
        }

        int sku;
        while (true) {
            sku = readInt(sc, "Ingrese SKU del Repuesto: ", 1, Integer.MAX_VALUE);
            if (svc.consultarRepuestoEnUbicacion(idVeh /* corregir: debería ser idUbicacion? para reserva global permite SKU global */, sku) == null)
                System.out.println("⚠ SKU inexistente para ese vehículo.");
            else break;
        }
        Repuesto r = svc.consultarRepuesto(sku);

        int cant;
        while (true) {
            cant = readInt(sc, String.format("Ingrese Cantidad a reservar (hasta %d): ", r.getCantidad()), 1, r.getCantidad());
            break;
        }

        svc.agregarReserva(idVeh, sku, cant);
        System.out.println("✔ Reserva agregada correctamente.");
    }

    private static void liberarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Reserva --");
        int idRes;
        while (true) {
            idRes = readInt(sc, "Ingrese ID de Reserva: ", 1, Integer.MAX_VALUE);
            if (svc.consultarReserva(idRes) == null) {
                System.out.println("⚠ Reserva inexistente.");
            } else break;
        }

        svc.liberarReserva(idRes);
        System.out.println("✔ Reserva liberada correctamente.");
    }

    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = sc.nextInt(); sc.nextLine();
                if (val < min || val > max) System.out.printf("Valor inválido: debe estar entre %d y %d.%n", min, max);
                else return val;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida: ingrese un número entero.");
                sc.nextLine();
            }
        }
    }
}