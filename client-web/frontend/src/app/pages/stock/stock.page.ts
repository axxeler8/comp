import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { InventarioService } from '../../services/inventario.service';

@Component({
  selector: 'app-stock',
  templateUrl: './stock.page.html',
  styleUrls: ['./stock.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule]
})
export class StockPage {
  sku: number = 0;
  cantidad: number = 0;
  resultado: string = '';

  constructor(private inventarioService: InventarioService) {}

  agregarStock() {
    this.inventarioService.agregarStock(this.sku, this.cantidad).subscribe({
      next: () => this.resultado = 'Stock actualizado',
      error: () => this.resultado = 'Error de conexión'
    });
  }
}