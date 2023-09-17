import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealI } from '../../models/interfaces';

@Component({
  selector: 'app-browse-meals',
  templateUrl: './browse-meals.component.html',
  styleUrls: ['./browse-meals.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
})

export class BrowseMealsComponent  implements OnInit {
  @Input() mealsData!: MealI;
  @Input() searchData!: MealI;
  @Input() Searched: boolean = false;

  item: MealI | undefined;
  popularMeals: MealI[] = [];
  thing: MealI | undefined;
  searchedMeals: MealI[] = [];
  isModalOpen = false;
  currentObject: any;
  fIns: String[] = [];
  fIng: String[] = [];

  constructor() { }

  ngOnInit() {
   // console.log(this.mealsData);
   this.item = this.mealsData;
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
