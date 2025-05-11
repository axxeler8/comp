import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface Repuesto {
  sku: number;
  nombre: string;
  stock: number;
  precio: number;
}

@Injectable({
  providedIn: 'root'
})
export class InventarioService {
  private apiUrl = 'http://localhost:1099/api/inventario'; // Ajusta el puerto según tu backend

  constructor(private http: HttpClient) { }

  getRepuestos(): Observable<Repuesto[]> {
    return this.http.get<Repuesto[]>(`${this.apiUrl}/repuestos`);
  }

  getRepuesto(sku: number): Observable<Repuesto> {
    return this.http.get<Repuesto>(`${this.apiUrl}/repuesto/${sku}`);
  }

  reservar(sku: number, cantidad: number): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/reservar`, { sku, cantidad });
  }

  agregarStock(sku: number, cantidad: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/stock`, { sku, cantidad });
  }
}