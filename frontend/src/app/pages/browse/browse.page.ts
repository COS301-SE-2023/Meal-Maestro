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
  
    // for (let index = 0; index < 8; index++) {
    //   this.mealGenerationservice.getPopularMeals().subscribe({
    //     next: (data) => {
    //       // Parse the JSON string into a JavaScript array of objects
    //       let mealsArray: MealBrowseI[] = JSON.parse(data);

    //       // Add the parsed mealsArray to the popularMeals array
    //       this.popularMeals = this.popularMeals.concat(mealsArray);

    //       console.log(this.popularMeals);
    //     },
    //     error: (err) => {
    //       this.errorHandlerService.presentErrorToast(
    //         'Error loading meal items', err
    //       );
    //     }
    //   })

    // }
    // for (let index = 0; index < 8; index++) {
    //   try {
    //     const data = await this.mealGenerationservice.getPopularMeals().toPromise();
    //     // Parse the JSON string into a JavaScript array of objects
    //     const mealsArray: MealBrowseI[] = JSON.parse(data);

    //     // Add the parsed mealsArray to the popularMeals array
    //     this.popularMeals = this.popularMeals.concat(mealsArray);

    //     console.log(this.popularMeals);
    //   } catch (err) {
    //     this.errorHandlerService.presentErrorToast(
    //       'Error loading meal items', err
    //     );
    //   }
    // }

    
  


  // for (let index = 0; index < 3; index++) {
    this.mealGenerationservice.getPopularMeals().subscribe({
      next: (data) => {
        this.popularMeals = this.popularMeals.concat(data);
       // const parsedData = JSON.parse(data);
        // if(Array.isArray(parsedData)){
        //   this.popularMeals = parsedData;
        // }
        // else {
        //   this.popularMeals =[parsedData];
        // }
        
        console.log(this.popularMeals);
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error loading meal items', err
        )
      }
    })
    
 // }
}

generateSearchedMeals(query: string): void {
  this.http
    .get<any>(`/api/getSearchedMeals?query=${query}`)
    .subscribe({
      next: (data) => {
        // Handle the data returned from the backend, if needed
        console.log(data);
      },
      error: (err) => {
        // Handle errors here
        console.error('Error searching meals:', err);
      },
    });
}

onSearch() {
  if (this.searchQuery && this.searchQuery.trim() !== '') {
    this.generateSearchedMeals(this.searchQuery);
  } else {
    console.log('Please enter a valid search query.');
  }
}

}
