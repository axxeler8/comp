package com.empresa.cliente;
import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;
import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
public class ConsoleClient {
  private static InventarioService svc;
  public static void main(String[] args) throws Exception {
    svc=(InventarioService)Naming.lookup("rmi://localhost:1099/InventarioService");
    Scanner sc=new Scanner(System.in);
    while(true){
      System.out.println("1) Listar todos 2) Consultar 3) Reservar 4) Agregar stock 5) Salir");
      switch(sc.nextInt()){
        case 1:
          List<Repuesto> list=svc.listarTodos(); list.forEach(r->System.out.println(r.getSku()+" - "+r.getNombre()+" stock:"+r.getStock())); break;
        case 2:
          System.out.print("SKU: ");int sku=sc.nextInt();Repuesto rp=svc.consultarRepuesto(sku);
          System.out.println(rp!=null?rp.getNombre()+" stock:"+rp.getStock():"No existe"); break;
        case 3:
          System.out.print("SKU y cant: ");sku=sc.nextInt();int c=sc.nextInt();
          System.out.println(svc.reservarRepuesto(sku,c)?"Reservado":"Fallo"); break;
        case 4:
          System.out.print("SKU y cant: ");sku=sc.nextInt();c=sc.nextInt();svc.agregarStock(sku,c);System.out.println("Hecho"); break;
        default: System.exit(0);
      }
    }
  }
}