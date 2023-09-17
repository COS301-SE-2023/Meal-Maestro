import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealI } from '../../models/interfaces';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';

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
  @Input() items!: MealI[];

  constructor(private addService: AddRecipeService) { }

  ngOnInit() {
   // console.log(this.mealsData);
    this.item = this.mealsData;

    if (this.item && this.item.instructions) {
      this.formatIns(this.item.instructions);
    }

    if (this.item && this.item.ingredients) {
      this.formatIng(this.item.ingredients);
    }
  }

  private formatIns(ins: string) {
    const insArr: string[] = ins.split(/\d+\.\s+/);
    this.fIns = insArr.filter(instruction => instruction.trim() !== '');
  }

  private formatIng(ing: string) {
    const ingArr: string[] = ing.split(/,[^()]*?(?![^(]*\))/);
    this.fIng = ingArr.map((ingredient) => ingredient.trim());
  }

  addRecipe(item: MealI) {
    this.addService.setRecipeItem(item);
  }

  notSaved(): boolean {
    return !this.items.includes(this.item);
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
