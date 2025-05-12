import { Routes, provideRouter } from '@angular/router';

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.page').then((m) => m.HomePage),
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: 'consultar',
    loadComponent: () => import('./pages/consultar/consultar.page').then( m => m.ConsultarPage)
  },
  {
    path: 'reservar',
    loadComponent: () => import('./pages/reservar/reservar.page').then( m => m.ReservarPage)
  },
  {
    path: 'stock',
    loadComponent: () => import('./pages/stock/stock.page').then( m => m.StockPage)
  }
];

export const appRoutingProviders = [provideRouter(routes)];