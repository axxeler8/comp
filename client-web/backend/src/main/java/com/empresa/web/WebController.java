package com.empresa.web;

import com.empresa.inventario.Repuesto;
import com.empresa.inventario.InventarioService;
import org.springframework.web.bind.annotation.*;
import java.rmi.RemoteException;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:8100") 
public class WebController {
    private final InventarioService svc;

    public WebController(InventarioService svc) {
        this.svc = svc;
    }

    @GetMapping("/repuestos")
    public List<Repuesto> all() {
        try {
            return svc.listarTodos();
        } catch (RemoteException e) {
            throw new RuntimeException("Error al listar repuestos", e);
        }
    }

    @GetMapping("/repuesto/{sku}")
    public Repuesto one(@PathVariable int sku) {
        try {
            return svc.consultarRepuesto(sku);
        } catch (RemoteException e) {
            throw new RuntimeException("Error al consultar SKU: " + sku, e);
        }
    }

    @PostMapping("/reservar")
    public boolean reservar(@RequestParam int sku, @RequestParam int cant) {
        try {
            return svc.reservarRepuesto(sku, cant);
        } catch (RemoteException e) {
            throw new RuntimeException("Error al reservar SKU: " + sku, e);
        }
    }

    @PostMapping("/stock")
    public void stock(@RequestParam int sku, @RequestParam int cant) {
        try {
            svc.agregarStock(sku, cant);
        } catch (RemoteException e) {
            throw new RuntimeException("Error al actualizar stock", e);
        }
    }
}