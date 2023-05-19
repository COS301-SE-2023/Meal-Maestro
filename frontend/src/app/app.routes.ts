import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./pages/tabs/tabs.routes').then((m) => m.routes),
  },  {
    path: 'shopping',
    loadComponent: () => import('./pages/shopping/shopping.page').then( m => m.ShoppingPage)
  },

];
