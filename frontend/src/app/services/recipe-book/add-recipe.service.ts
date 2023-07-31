import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { RecipeItemI } from '../../models/recipeItem.model';

@Injectable({
  providedIn: 'root'
})
export class AddRecipeService {
  private recipeSource: BehaviorSubject<RecipeItemI | null> = new BehaviorSubject<RecipeItemI | null>(null);
  constructor() { }

  recipeItem$ = this.recipeSource.asObservable();

  setRecipeItem(recipeItem: RecipeItemI): void {
    this.recipeSource.next(recipeItem);
  }
}
