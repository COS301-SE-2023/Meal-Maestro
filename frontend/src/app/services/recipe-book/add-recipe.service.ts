import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { MealI } from '../../models/meal.model';

@Injectable({
  providedIn: 'root'
})
export class AddRecipeService {
  private recipeSource: BehaviorSubject<MealI | undefined> = new BehaviorSubject<MealI | undefined>(undefined);
  constructor() { }

  recipeItem$ = this.recipeSource.asObservable();

  setRecipeItem(recipeItem: MealI | undefined): void {
    this.recipeSource.next(recipeItem);
  }
}
