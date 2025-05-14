package com.empresa.operario;

import com.empresa.inventario.InventarioService;
import java.rmi.Naming;
import java.util.Scanner;

public class OperarioConsole {
    public static void main(String[] args) throws Exception {
        // Conexión RMI
        InventarioService svc = (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Operario ---");
            System.out.println("1) Agregar repuesto");
            System.out.println("2) Liberar repuesto");
            System.out.println("3) Agregar reserva");
            System.out.println("4) Liberar reserva");
            System.out.println("5) Salir");
            System.out.print("Seleccione una opción: ");
            int op = sc.nextInt();
            sc.nextLine(); // limpia el buffer

            switch (op) {
                case 1:
                    System.out.println("\n-- Agregar Repuesto --");
                    System.out.print("Ingrese ID de Ubicación: ");
                    int idUb = sc.nextInt();
                    System.out.print("Ingrese SKU: ");
                    int skuAdd = sc.nextInt();
                    System.out.print("Ingrese Cantidad: ");
                    int cantAdd = sc.nextInt();
                    System.out.print("Ingrese Precio: ");
                    int precio = sc.nextInt();
                    sc.nextLine();
                    System.out.print("¿Disponible? (si/no): ");
                    boolean disponible = "si".equalsIgnoreCase(sc.nextLine().trim());
                    System.out.print("Ingrese Nombre del Repuesto: ");
                    String nombre = sc.nextLine();

                    svc.agregarRepuesto(idUb, skuAdd, cantAdd, precio, disponible, nombre);
                    System.out.println("✔ Repuesto agregado correctamente.");
                    break;

                case 2:
                    System.out.println("\n-- Liberar Repuesto --");
                    System.out.print("Ingrese ID de Ubicación: ");
                    int idUbLib = sc.nextInt();
                    System.out.print("Ingrese SKU: ");
                    int skuLib = sc.nextInt();
                    System.out.print("Ingrese Cantidad a liberar: ");
                    int cantLib = sc.nextInt();

                    svc.liberarRepuesto(idUbLib, skuLib, cantLib);
                    System.out.println("✔ Repuesto liberado correctamente.");
                    break;

                case 3:
                    System.out.println("\n-- Agregar Reserva --");
                    System.out.print("Ingrese ID de Vehículo: ");
                    int idVeh = sc.nextInt();
                    System.out.print("Ingrese SKU del Repuesto: ");
                    int skuRes = sc.nextInt();
                    System.out.print("Ingrese Cantidad a reservar: ");
                    int cantRes = sc.nextInt();

                    svc.agregarReserva(idVeh, skuRes, cantRes);
                    System.out.println("✔ Reserva agregada correctamente.");
                    break;

                case 4:
                    System.out.println("\n-- Liberar Reserva --");
                    System.out.print("Ingrese ID de Reserva: ");
                    int idRes = sc.nextInt();

                    svc.liberarReserva(idRes);
                    System.out.println("✔ Reserva liberada correctamente.");
                    break;

                case 5:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("¡Opción inválida, intente de nuevo!");
            }
        }
    }
}
