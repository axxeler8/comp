package com.empresa.cliente;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ClienteConsole {
    public static void main(String[] args) throws Exception {
        InventarioService svc =
            (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Cliente ---");
            System.out.println("1) Ver repuestos");
            System.out.println("2) Consultar repuesto por SKU");
            System.out.println("3) Salir");
            System.out.print("Seleccione una opción: ");
            int op = sc.nextInt();
            sc.nextLine(); // limpia buffer

            switch (op) {
                case 1:
                    System.out.println("\n-- Lista de Repuestos --");
                    List<Repuesto> todos = svc.verRepuestos();
                    if (todos.isEmpty()) {
                        System.out.println("No hay repuestos registrados.");
                    } else {
                        todos.forEach(r -> {
                            System.out.printf("SKU: %d | Nombre: %s | Cantidad: %d | Precio: %d | Disponible: %s%n",
                                r.getSku(), r.getNombre(), r.getCantidad(),
                                r.getPrecio(), r.isDisponible() ? "si" : "no");
                        });
                    }
                    break;

                case 2:
                    System.out.println("\n-- Buscar Repuesto --");
                    System.out.print("Ingrese SKU: ");
                    int sku = sc.nextInt();
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
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("¡Opción inválida, intente de nuevo!");
            }
        }
    }
}
