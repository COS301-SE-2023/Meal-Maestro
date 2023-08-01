import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActionSheetController, IonicModule } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';
import { AuthenticationService, ErrorHandlerService, RecipeBookApiService } from '../../services/services';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';
import { MealI } from '../../models/meal.model';

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.page.html',
  styleUrls: ['./recipe-book.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, RecipeItemComponent]
})
export class RecipeBookPage implements OnInit {
  @ViewChild(RecipeItemComponent) recipeItem!: RecipeItemComponent;
  public items: MealI[] = [];

  constructor(private recipeService: RecipeBookApiService, 
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService,
    private actionSheetController: ActionSheetController,
    private addService: AddRecipeService) { }

  async ionViewWillEnter() {
    this.getRecipes();
  }

  async addRecipe(item: MealI) {   
    this.recipeService.addRecipe(item).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body) {
              this.getRecipes();             
              this.errorHandlerService.presentSuccessToast(item.name + " added to Recipe Book");
            }
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorised access. Please log in again.',
              err
            )
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error adding item to your Recipe Book',
              err
            )
          }
        }
      });
  }

  async getRecipes() {
    this.recipeService.getAllRecipes().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.items = response.body;
            this.recipeItem.passItems(this.items);
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            "Unauthorised access. Please log in again",
            err
          )
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading saved recipes',
            err
          )
        }
      }
    })
  }

  async confirmRemove(event: Event, recipe: MealI) {
    event.stopPropagation();

    const actionSheet = await this.actionSheetController.create({
      header: `Are you sure you want to remove ${recipe.name} from your recipe book?`,
      buttons: [
        {
          text: 'Delete',
          role: 'destructive',
          handler: () => {
            this.removeRecipe(recipe);
          }
        },
        {
          text: 'Cancel',
          role: 'cancel'
        }
      ]
    });
  
    await actionSheet.present();
  }

  async removeRecipe(recipe: MealI) {
    this.recipeService.removeRecipe(recipe).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.errorHandlerService.presentSuccessToast(
            `Successfully removed ${recipe.name}`
          )
          this.getRecipes();
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorised access. Please log in again',
            err
          )
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error removing recipe from Recipe Book',
            err
          )
        }
      }
    });
  }

  handleEvent(data: MealI) {
    this.addRecipe(data);
  }

  ngOnInit() {
    this.addService.recipeItem$.subscribe((recipeItem) => {
      if (recipeItem) {
        this.addRecipe(recipeItem);
      }
    });
  }
   
}
