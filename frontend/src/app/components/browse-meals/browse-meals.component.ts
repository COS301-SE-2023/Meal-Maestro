import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealBrowseI } from '../../models/mealBrowse.model';

@Component({
  selector: 'app-browse-meals',
  templateUrl: './browse-meals.component.html',
  styleUrls: ['./browse-meals.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
})

export class BrowseMealsComponent  implements OnInit {
  @Input() mealsData!: MealBrowseI;
  @Input() searchData!: MealBrowseI;
  @Input() Searched: boolean = false;

  item: MealBrowseI | undefined;
  popularMeals: MealBrowseI[] = [];
  thing: MealBrowseI | undefined;
  searchedMeals: MealBrowseI[] = [];
  isModalOpen = false;
  currentObject: any;

  constructor() { }

  ngOnInit() {
   // console.log(this.mealsData);
  }

  setCurrent(o : any) {
    this.currentObject = o;
  }

  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
}
