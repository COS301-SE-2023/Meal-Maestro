import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'app',
    loadChildren: () =>
      import('./pages/tabs/tabs.routes').then((m) => m.routes),
  },
  {
    path: 'acc-profile',
    loadComponent: () =>
      import('./pages/acc-profile/acc-profile.page').then(
        (m) => m.AccProfilePage
      ),
  },
  {
    path: '',
    loadComponent: () =>
      import('./pages/login/login.page').then((m) => m.LoginPage),
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./pages/signup/signup.page').then((m) => m.SignupPage),
  },
  {
    path: 'browse',
    loadComponent: () =>
      import('./pages/browse/browse.page').then((m) => m.BrowsePage),
  },
  {
    path: 'recipe-book',
    loadComponent: () =>
      import('./pages/recipe-book/recipe-book.page').then(
        (m) => m.RecipeBookPage
      ),
  },
];
