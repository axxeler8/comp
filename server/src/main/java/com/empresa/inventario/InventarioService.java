package com.empresa.inventario;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
public interface InventarioService extends Remote {
  Repuesto consultarRepuesto(int sku) throws RemoteException;
  boolean reservarRepuesto(int sku, int cantidad) throws RemoteException;
  void agregarStock(int sku, int cantidad) throws RemoteException;
  void liberarRepuesto(int sku, int cantidad) throws RemoteException;
  List<Repuesto> listarTodos() throws RemoteException;
}