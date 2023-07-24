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

  
  constructor(public r : Router,
    private mealGenerationservice:MealGenerationService,
    private errorHandlerService:ErrorHandlerService) { }

 async ngOnInit() {
  for (let index = 0; index < 8; index++) {
    this.mealGenerationservice.getPopularMeals().subscribe({
      next: (data) => {
        if(Array.isArray(data)){
          this.popularMeals = data;
        }
        else {
          this.popularMeals =[data];
        }
        
        console.log(this.popularMeals);
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error loading meal items', err
        )
      }
    })
    
  }
}

}
