import { Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

export const routes: Routes = [
  {
    path: 'tabs',
    component: TabsPage,
    children: [
      {
        path: 'home',
        loadComponent: () =>
          import('../home/home.page').then((m) => m.HomePage),
      },
      {
        path: 'pantry',
        loadComponent: () =>
          import('../pantry/pantry.page').then((m) => m.PantryPage),
      },
      {
        path: 'profile',
        loadComponent: () =>
          import('../profile/profile.page').then((m) => m.ProfilePage),
      },
      // {
      //   path: 'signup',
      //   loadComponent: () =>
      //     import('../signup/signup.page').then((m) => m.SignupPage),
      // },
      {
        path: '',
        redirectTo: '/tabs/home',
        pathMatch: 'full',
      },
      
    ],
  },
  {
    path: '',
    redirectTo: '/tabs/home',
    pathMatch: 'full',
  },
];
