import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventarioService } from '../../services/inventario.service';
import { IonContent, IonHeader, IonToolbar, IonTitle, IonItem, IonList, IonButton} from '@ionic/angular/standalone';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, IonItem, IonList, IonButton, CommonModule, RouterLink]
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