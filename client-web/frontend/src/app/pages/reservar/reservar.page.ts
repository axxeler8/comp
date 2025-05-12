import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { InventarioService } from '../../services/inventario.service';

@Component({
  selector: 'app-reservar',
  templateUrl: './reservar.page.html',
  styleUrls: ['./reservar.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule]
})
export class ReservarPage {
  sku: number = 0;
  cantidad: number = 0;
  resultado: string = '';

  constructor(private inventarioService: InventarioService) {}

  reservar() {
    this.inventarioService.reservar(this.sku, this.cantidad).subscribe({
      next: (exito) => this.resultado = exito ? 'Reserva exitosa' : 'Error en reserva',
      error: () => this.resultado = 'Error de conexión'
    });
  }
}