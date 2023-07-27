import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { BrowseMealsComponent } from '../../components/browse-meals/browse-meals.component';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { ErrorHandlerService } from '../../services/services';
import { DaysMealsI } from '../../models/daysMeals.model';
import { MealBrowseI } from '../../models/mealBrowse.model';
import { HttpClient } from '@angular/common/http'; // Import HttpClient to make HTTP requests

@Component({
  selector: 'app-browse',
  templateUrl: './browse.page.html',
  styleUrls: ['./browse.page.scss'],
  standalone: true,
  imports: [IonicModule, BrowseMealsComponent, CommonModule ]
})
export class BrowsePage implements OnInit{
  // meals: DaysMealsI[];

  popularMeals: MealBrowseI[] = [];
  searchedMeals : MealBrowseI[] = [];
  noResultsFound: boolean = false;
  Searched: boolean = false;

  searchQuery: string='';
  searchResults: any;
  
  constructor(public r : Router,
    private mealGenerationservice:MealGenerationService,
    private errorHandlerService:ErrorHandlerService,
    private http: HttpClient) 
    { 
      this.searchQuery = '';
    }

 async ngOnInit() {
  
    this.mealGenerationservice.getPopularMeals().subscribe({
      next: (data) => {
        this.Searched = false;
        this.popularMeals = this.popularMeals.concat(data);
   
        console.log(this.popularMeals);
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error loading meal items', err
        )
      }

      
    })
 
}
 

// Function to handle the search bar input event
onSearch(event: Event) {
  // Get the search query from the event object
  
  const customEvent = event as CustomEvent<any>;
  const query: string = customEvent.detail.value;
 // const query: string = event.detail.value;
 // this.searchQuery = event.detail.value;

  // Call the getSearchedMeals function with the new search query
 // this.mealGenerationservice.getSearchedMeals(query).subscribe;
  this.mealGenerationservice.getSearchedMeals(query).subscribe({
    next: (data) => {
       this.Searched = true;

      if (data.length === 0) {
        this.noResultsFound = true;
       // console.log(this.searchedMeals);
      }
      else {
        //this.Searched = true;
        this.noResultsFound = false;
        this.searchedMeals = data;
        console.log(this.searchedMeals);
      }
      
    },
    error: (err) => {
      this.errorHandlerService.presentErrorToast('Error loading meal items', err);
    },
  });

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

