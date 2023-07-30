import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActionSheetController, AlertController, IonicModule } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';
import { RecipeItemI } from '../../models/recipeItem.model';
import { AuthenticationService, ErrorHandlerService, RecipeBookApiService } from '../../services/services';
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

  constructor(private recipeService: RecipeBookApiService, 
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService,
    private actionSheetController: ActionSheetController) { }

  async ionViewWillEnter() {
    this.getRecipes();
  }

  async getRecipes() {
    this.recipeService.getAllRecipes().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.items = response.body;
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

  async confirmRemove(event: Event, recipe: RecipeItemI) {
    event.stopPropagation();

    const actionSheet = await this.actionSheetController.create({
      header: `Are you sure you want to remove ${recipe.title} from your recipe book?`,
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

  async removeRecipe(recipe: RecipeItemI) {
    this.recipeService.removeRecipe(recipe).subscribe({
      next: (response) => {
        if (response.status === 200) {
          this.errorHandlerService.presentSuccessToast(
            `Successfully removed ${recipe.title}`
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

  ngOnInit() {
  }

}
