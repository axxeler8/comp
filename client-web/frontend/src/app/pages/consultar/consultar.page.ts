import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { InventarioService } from '../../services/inventario.service';

@Component({
  selector: 'app-consultar',
  templateUrl: './consultar.page.html',
  styleUrls: ['./consultar.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule]
})
export class ConsultarPage {
  sku: number = 0; // <-- ¡Declara la propiedad aquí!
  repuesto: any;

  constructor(private inventarioService: InventarioService) {}

  consultar() {
    this.inventarioService.getRepuesto(this.sku).subscribe(data => {
      this.repuesto = data;
    });
  }
}