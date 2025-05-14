// ClienteConsole.java
package com.empresa.cliente;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;

import java.rmi.Naming;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ClienteConsole {
    public static void main(String[] args) throws Exception {
        InventarioService svc = (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Cliente ---");
            System.out.println("1) Ver repuestos");
            System.out.println("2) Consultar repuesto por SKU");
            System.out.println("3) Salir");

            int op = readInt(sc, "Seleccione una opción: ", 1, 3);

            switch (op) {
                case 1:
                    mostrarRepuestos(svc);
                    break;
                case 2:
                    buscarRepuesto(svc, sc);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);
            }
        }
    }

    private static void mostrarRepuestos(InventarioService svc) throws Exception {
        System.out.println("\n-- Lista de Repuestos --");
        List<Repuesto> todos = svc.verRepuestos();
        if (todos.isEmpty()) {
            System.out.println("No hay repuestos registrados.");
        } else {
            todos.forEach(r -> System.out.printf("SKU: %d | Nombre: %s | Cantidad: %d | Precio: %d | Disponible: %s%n",
                r.getSku(), r.getNombre(), r.getCantidad(), r.getPrecio(), r.isDisponible() ? "si" : "no"));
        }
    }

    private static void buscarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Buscar Repuesto --");
        int sku = readInt(sc, "Ingrese SKU: ", 1, Integer.MAX_VALUE);
        Repuesto r = svc.consultarRepuesto(sku);
        if (r == null) {
            System.out.printf("⚠ No existe ningún repuesto con SKU %d.%n", sku);
        } else {
            System.out.println("Repuesto encontrado:");
            System.out.printf("  SKU: %d%n", r.getSku());
            System.out.printf("  Nombre: %s%n", r.getNombre());
            System.out.printf("  Cantidad: %d%n", r.getCantidad());
            System.out.printf("  Precio: %d%n", r.getPrecio());
            System.out.printf("  Disponible: %s%n", r.isDisponible() ? "si" : "no");
        }
    }

    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = sc.nextInt(); sc.nextLine();
                if (val < min || val > max) {
                    System.out.printf("Valor inválido: debe estar entre %d y %d.%n", min, max);
                } else {
                    return val;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida: ingrese un número entero.");
                sc.nextLine();
            }
        }
    }
}