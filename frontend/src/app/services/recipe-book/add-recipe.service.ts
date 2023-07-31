import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { RecipeItemI } from '../../models/recipeItem.model';
import { MealI } from '../../models/meal.model';

@Injectable({
  providedIn: 'root'
})
export class AddRecipeService {
  private recipeSource: BehaviorSubject<MealI | null> = new BehaviorSubject<MealI | null>(null);
  constructor() { }

  recipeItem$ = this.recipeSource.asObservable();

  setRecipeItem(recipeItem: MealI): void {
    this.recipeSource.next(recipeItem);
  }
}
