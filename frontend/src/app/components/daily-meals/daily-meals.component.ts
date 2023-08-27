import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule, IonicSlides } from '@ionic/angular';
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
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DailyMealsComponent implements OnInit {
  breakfast: string = 'breakfast';
  lunch: string = 'lunch';
  dinner: string = 'dinner';
  mealDate: string | undefined;
  // @Input() todayData!: MealI[];
  @Input() dayData!: DaysMealsI;
  // daysMeals: DaysMealsI[] = [];
  isBreakfastModalOpen = false;
  isLunchModalOpen = false;
  isDinnerModalOpen = false;
  isModalOpen = false;
  currentObject: DaysMealsI | undefined;
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
  constructor(
    public r: Router,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService,
    private addService: AddRecipeService
  ) {}

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

  handleArchive(meal: string) {
    // Function to handle the "Archive" option action
    var recipe: MealI | undefined;

    if (meal == 'breakfast') recipe = this.dayData.breakfast;
    else if (meal == 'lunch') recipe = this.dayData.lunch;
    else recipe = this.dayData.dinner;

    this.addService.setRecipeItem(recipe);
  }

  async handleRegenerate(meal: MealI | undefined) {
    // Function to handle the "Sync" option action
    console.log('Regen option clicked');
    // Add your custom logic here
    if (meal) {
      this.mealGenerationservice.regenerate(meal).subscribe({
        next: (data) => {
          if (data.body) {
            console.log(data.body);
            if (meal.type == 'breakfast') {
              this.dayData.breakfast = data.body;
            } else if (meal.type == 'lunch') {
              this.dayData.lunch = data.body;
            } else if (meal.type == 'dinner') {
              this.dayData.dinner = data.body;
            }
          }
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error regenerating meal items',
            err
          );
        },
      });
    }
  }

  setCurrent(o: any) {
    this.currentObject = o;
  }
}
