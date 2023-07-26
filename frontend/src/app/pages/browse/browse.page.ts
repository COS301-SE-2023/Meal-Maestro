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
 

// onSearch() {
//   if (this.searchQuery && this.searchQuery.trim() !== '') {
//     this.generateSearchedMeals(this.searchQuery);
//   } else {
//     console.log('Please enter a valid search query.');
//   }
// }

}
