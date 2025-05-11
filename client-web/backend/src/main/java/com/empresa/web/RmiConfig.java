package com.empresa.web;
import com.empresa.inventario.InventarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.rmi.Naming;
@Configuration
public class RmiConfig {
  @Bean
  public InventarioService inventarioService() throws Exception {
    return (InventarioService)Naming.lookup("rmi://localhost:1099/InventarioService");
  }
}