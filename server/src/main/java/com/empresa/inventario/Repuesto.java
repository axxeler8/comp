package com.empresa.inventario;
import java.io.Serializable;
public class Repuesto implements Serializable {
  private int sku;
  private String nombre;
  private String descripcion;
  private double precio;
  private int stock;
  public Repuesto() {}
  public Repuesto(int sku, String nombre, String descripcion, double precio, int stock) {
    this.sku = sku; this.nombre = nombre; this.descripcion = descripcion;
    this.precio = precio; this.stock = stock;
  }
  // getters y setters
  public int getSku(){return sku;} public void setSku(int s){sku=s;}
  public String getNombre(){return nombre;} public void setNombre(String n){nombre=n;}
  public String getDescripcion(){return descripcion;} public void setDescripcion(String d){descripcion=d;}
  public double getPrecio(){return precio;} public void setPrecio(double p){precio=p;}
  public int getStock(){return stock;} public void setStock(int s){stock=s;}
}