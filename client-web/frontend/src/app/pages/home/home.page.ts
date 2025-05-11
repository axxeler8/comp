import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventarioService } from '../../services/inventario.service';
import { IonContent, IonHeader, IonToolbar, IonTitle, IonItem, IonList, IonButton, IonText } from '@ionic/angular/standalone';
@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, IonItem, IonList, IonButton, CommonModule, IonText]
})
export class HomePage {
  repuestos: any[] = [];

  constructor(private inventarioService: InventarioService) {
    this.cargarRepuestos();
  }

  cargarRepuestos() {
    this.inventarioService.getRepuestos().subscribe(data => {
      this.repuestos = data;
    });
  }
}