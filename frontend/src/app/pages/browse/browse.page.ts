import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { BrowseMealsComponent } from '../../components/browse-meals/browse-meals.component';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import {
  AuthenticationService,
  ErrorHandlerService,
  RecipeBookApiService,
} from '../../services/services';
import { MealI } from '../../models/interfaces';

@Component({
  selector: 'app-browse',
  templateUrl: './browse.page.html',
  styleUrls: ['./browse.page.scss'],
  standalone: true,
  imports: [IonicModule, BrowseMealsComponent, CommonModule],
})
export class BrowsePage implements OnInit {
  popularMeals: MealI[] = [];
  searchedMeals: MealI[] = [];
  noResultsFound: boolean = false;
  Searched: boolean = false;
  Loading: boolean = false;
  searchQuery: string = '';
  searchResults: any;
  recipeItems: MealI[] = [];

  constructor(
    public r: Router,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService,
    private recipeService: RecipeBookApiService
  ) {
    this.searchQuery = '';
  }

  async ngOnInit() {
    this.initialiseItems();
  }

  initialiseItems() {
    this.mealGenerationservice.getPopularMeals().subscribe({
      next: (data) => {
        this.Searched = false;
        this.popularMeals = data;

        console.log(this.popularMeals);
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading meal items',
            err
          );
        }
      },
    });
  }

  // Function to handle the search bar input event
  onSearch(event: Event) {
    // Get the search query from the event object

    const customEvent = event as CustomEvent<any>;
    const query: string = customEvent.detail.value;

    if (query == '') {
      this.initialiseItems();
      return;
    }

    // const query: string = event.detail.value;
    // this.searchQuery = event.detail.value;

    // Call the getSearchedMeals function with the new search query
    // this.mealGenerationservice.getSearchedMeals(query).subscribe;
    this.mealGenerationservice.getSearchedMeals(query).subscribe({
      next: (data) => {
        this.Searched = true;

        if (data.length === 0) {
          this.noResultsFound = true;
        } else {
          this.noResultsFound = false;
          this.searchedMeals = data;
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading meal items',
            err
          );
        }
      },
    });
  }

  cancel() {
    this.Searched = false;
  }

  RefreshMeals(event: any) {
    this.Loading = true;
    setTimeout(() => {
      this.Loading = false;
      event.target.complete();
    }, 2000);
  }

  // generateSearchMeals(query: string) {
  //   // Call the service function to get the searched meals with the provided query
  //   this.mealGenerationservice.getSearchedMeals(query).subscribe({
  //     next: (data) => {
  //       // Update the searchedMeals array with the data returned from the service
  //       this.searchedMeals = data;
  //       console.log(this.searchedMeals);
  //     },
  //   })
  // }
}
