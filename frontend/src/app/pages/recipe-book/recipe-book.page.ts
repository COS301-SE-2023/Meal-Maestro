import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
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
    private auth: AuthenticationService) { }

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

  ngOnInit() {
  }

}
