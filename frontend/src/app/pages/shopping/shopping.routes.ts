import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingPage } from './shopping.page';

const routes: Routes = [
  {
    path: '',
    component: ShoppingPage,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShoppingPageRoutingModule 
{}
