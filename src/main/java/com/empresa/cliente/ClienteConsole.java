package com.empresa.cliente;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ClienteConsole {
    public static void main(String[] args) throws Exception {
        InventarioService svc = (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Cliente ---");
            System.out.println("1) Ver repuestos");
            System.out.println("2) Consultar por SKU");
            System.out.println("3) Salir");
            int op = Integer.parseInt(sc.nextLine());
            if (op == 1) {
                mostrarRepuestos(svc);
            } else if (op == 2) {
                System.out.print("SKU: ");
                int sku = Integer.parseInt(sc.nextLine());
                Repuesto r = svc.consultarRepuesto(sku);
                if (r != null) {
                    System.out.printf(
                        "SKU: %d%nNombre: %s%nCantidad: %d%nPrecio: %d%nCategoría: %s%nDisponible: %b%n",
                        r.getSku(), r.getNombre(), r.getCantidad(),
                        r.getPrecio(), r.getCategoria(), r.isDisponible()
                    );
                } else {
                    System.out.println("No existe ese SKU.");
                }
            } else {
                break;
            }
        }
        sc.close();
    }

    private static void mostrarRepuestos(InventarioService svc) throws Exception {
        List<Repuesto> list = svc.verRepuestos();
        System.out.println("\n-- Lista de Repuestos --");
        for (Repuesto r : list) {
            System.out.printf(
                "[%d] %s | Cant: %d | Precio: %d | Cat: %s | Disp: %b%n",
                r.getSku(), r.getNombre(), r.getCantidad(),
                r.getPrecio(), r.getCategoria(), r.isDisponible()
            );
        }
    }
}
