import { CommonModule } from '@angular/common';
import { Component, OnInit, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealBrowseI } from '../../models/mealBrowse.model';

@Component({
  selector: 'app-browse-meals',
  templateUrl: './browse-meals.component.html',
  styleUrls: ['./browse-meals.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
  // template: `
  //   <app-browse-meals *ngFor="let meal of meals"></app-browse-meals>
  // `,
  // schemas: [NO_ERRORS_SCHEMA],
})
export class BrowseMealsComponent  implements OnInit {

  @Input() mealsData!: MealBrowseI[];
  isModalOpen = false;
  currentObject :any
  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  constructor() { }

  ngOnInit() {}

  setCurrent(o : any) {
    this.currentObject = o;
  }
}
