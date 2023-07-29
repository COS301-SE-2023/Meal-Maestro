import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';
import { RecipeItemI } from '../../models/recipeItem.model';
import { RecipeBookApiService } from '../../services/services';
import { catchError, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.page.html',
  styleUrls: ['./recipe-book.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, RecipeItemComponent]
})
export class RecipeBookPage implements OnInit {
  items: RecipeItemI[] = [];

  constructor(private recipeService: RecipeBookApiService) { }

  async ionViewWillEnter() {
    try {
      const recipes = await this.getRecipes();
      this.items = recipes;
    } catch (error) {
      console.log("An error in ionViewWillEnter rb.page: " + error);
    }
  }

  async getRecipes() {
    try {
      const recipes = await firstValueFrom(
        this.recipeService.getAllRecipes().pipe(
          catchError((error) => {
            throw error;
          })
        )
      );
      return recipes;
    } catch (error) {
      throw error;
    }
  }

  ngOnInit() {
  }

}
