
import { Routes } from '@angular/router';


export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./pages/tabs/tabs.routes').then((m) => m.routes),
  },
  {
    path: 'acc-profile',
    loadComponent: () => import('./pages/acc-profile/acc-profile.page').then( m => m.AccProfilePage)
  },
];



