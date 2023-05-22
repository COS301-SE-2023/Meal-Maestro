
import { Routes } from '@angular/router';


export const routes: Routes = [
  {
    path: 'app',
    loadChildren: () => import('./pages/tabs/tabs.routes').then((m) => m.routes),
  },
  {
    path: 'acc-profile',
    loadComponent: () => import('./pages/acc-profile/acc-profile.page').then( m => m.AccProfilePage)
  },
    path: '',
    loadComponent: () => import('./pages/login/login.page').then( m => m.LoginPage)
  },
];



