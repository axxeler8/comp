package com.empresa.inventario;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
public class ServerMain {
  public static void main(String[] args) {
    try {
      LocateRegistry.createRegistry(1099);
      InventarioService svc=new InventarioServiceImpl();
      Naming.rebind("rmi://localhost:1099/InventarioService", svc);
      System.out.println("Servidor iniciado");
    } catch(Exception e){e.printStackTrace();}
  }
}