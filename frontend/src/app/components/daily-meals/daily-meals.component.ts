import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule, IonicSlides } from '@ionic/angular';
import { MealI } from '../../models/meal.model';
import { Router } from '@angular/router';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { DaysMealsI } from '../../models/daysMeals.model';
import { ErrorHandlerService } from '../../services/services';
import { MealI, RecipeItemI } from '../../models/interfaces';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';

@Component({
  selector: 'app-daily-meals',
  templateUrl: './daily-meals.component.html',
  styleUrls: ['./daily-meals.component.scss'],
  standalone : true,
  imports: [CommonModule, IonicModule],
})
export class DailyMealsComponent  implements OnInit {

  breakfast: string = "breakfast";
  lunch: string  = "lunch";
  dinner: string  = "dinner";
  mealDate: string | undefined;
  @Input() todayData!: MealI[];
  @Input() dayData!: DaysMealsI;
  item: DaysMealsI | undefined;
  daysMeals: DaysMealsI[] = [] ;
  meals:MealI[] = [];
  isBreakfastModalOpen = false;
  isLunchModalOpen = false;
  isDinnerModalOpen = false;
  isModalOpen = false;
  currentObject :DaysMealsI | undefined
  setOpen(isOpen: boolean, mealType: string) {
    if (mealType === 'breakfast') {
      this.isBreakfastModalOpen = isOpen;
      if (isOpen) {
        this.setCurrent(this.dayData?.breakfast);
      }
    } else if (mealType === 'lunch') {
      this.isLunchModalOpen = isOpen;
      if (isOpen) {
        this.setCurrent(this.dayData?.lunch);
      }
    } else if (mealType === 'dinner') {
      this.isDinnerModalOpen = isOpen;
      if (isOpen) {
        this.setCurrent(this.dayData?.dinner);
      }
    }
  }
  constructor(public r : Router
    , private mealGenerationservice:MealGenerationService
    , private errorHandlerService:ErrorHandlerService,
    private addService: AddRecipeService) {}

  ngOnInit() {
    // this.mealGenerationservice.getDailyMeals().subscribe({
    //   next: (data) => {
    //     this.dayData = data;
        
    //   },
    //   error: (err) => {
    //     this.errorHandlerService.presentErrorToast(
    //       'Error loading meal items', err
    //     )
    //   }
    // })


  }

  handleArchive(meal:string) {
    // Function to handle the "Archive" option action
    var recipe: MealI | undefined;

    if (meal == "breakfast")
      recipe = this.dayData.breakfast;
    else if (meal == "lunch")
      recipe = this.dayData.lunch;
    else recipe = this.dayData.dinner;   

    const item = {
      title: recipe?.name;
      image: recipe.im
    }

    this.addService.setRecipeItem(recipe);
  }

  async handleSync(meal:string) {
    // Function to handle the "Sync" option action
    console.log('Sync option clicked');
    // Add your custom logic here
    this.mealGenerationservice.handleArchive(this.dayData, meal).subscribe({
      next: (data) => {
        data.mealDate = this.dayData.mealDate;
        this.dayData = data;
        
        console.log(this.meals);
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error regenerating meal items', err
        )
      }
    })
  }

  setCurrent(o : any) {
    this.currentObject = o;
  }

}
