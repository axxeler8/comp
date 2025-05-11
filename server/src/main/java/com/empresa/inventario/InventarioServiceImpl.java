package com.empresa.inventario;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

public class InventarioServiceImpl extends UnicastRemoteObject implements InventarioService {
  private static final Logger log = LoggerFactory.getLogger(InventarioServiceImpl.class);
  private Connection conn;
  public InventarioServiceImpl() throws RemoteException {
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario?useSSL=false", "root", "axeler8");
    } catch (SQLException e) {
      log.error("Error BD", e);
      throw new RemoteException("BD error", e);
    }
  }

  @Override
  public Repuesto consultarRepuesto(int sku) throws RemoteException {
    try (PreparedStatement ps=conn.prepareStatement("SELECT * FROM Repuesto WHERE sku=?")){
      ps.setInt(1,sku);
      ResultSet rs=ps.executeQuery();
      if(rs.next()) return new Repuesto(rs.getInt("sku"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("precio"), rs.getInt("stock"));
    } catch(Exception e){log.error("consultar",e);} return null;
  }

  @Override
  public boolean reservarRepuesto(int sku, int cantidad) throws RemoteException {
    try {
      conn.setAutoCommit(false);
      Repuesto r=consultarRepuesto(sku);
      if(r==null || r.getStock()<cantidad){ conn.rollback(); return false; }
      try(PreparedStatement ps=conn.prepareStatement("UPDATE Repuesto SET stock=stock-? WHERE sku=?")){
        ps.setInt(1,cantidad); ps.setInt(2,sku); ps.executeUpdate();
      }
      try(PreparedStatement ps2=conn.prepareStatement("INSERT INTO Reserva(sku,cantidad) VALUES(?,?)")){
        ps2.setInt(1,sku); ps2.setInt(2,cantidad); ps2.executeUpdate();
      }
      conn.commit(); return true;
    } catch(Exception e){log.error("reservar",e); try{conn.rollback();}catch(SQLException ex){} return false; }
  }

  @Override public void agregarStock(int sku,int cantidad) throws RemoteException {
    try(PreparedStatement ps=conn.prepareStatement("UPDATE Repuesto SET stock=stock+? WHERE sku=?")){
      ps.setInt(1,cantidad); ps.setInt(2,sku); ps.executeUpdate();
    } catch(Exception e){log.error("agregar",e);} }

  @Override public void liberarRepuesto(int sku,int cantidad) throws RemoteException {
    agregarStock(sku, cantidad);
  }

  @Override public List<Repuesto> listarTodos() throws RemoteException {
    List<Repuesto> list=new ArrayList<>();
    try(Statement st=conn.createStatement(); ResultSet rs=st.executeQuery("SELECT * FROM Repuesto")){
      while(rs.next()) list.add(new Repuesto(rs.getInt("sku"),rs.getString("nombre"),rs.getString("descripcion"),rs.getDouble("precio"),rs.getInt("stock")));
    } catch(Exception e){log.error("listar",e);} return list;
  }
}