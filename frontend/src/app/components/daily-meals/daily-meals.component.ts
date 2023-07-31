import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule, IonicSlides } from '@ionic/angular';
import { MealI } from '../../models/meal.model';
import { Router } from '@angular/router';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { DaysMealsI } from '../../models/daysMeals.model';
import { ErrorHandlerService } from '../../services/services';

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

  @Input() todayData!: MealI[];
  @Input() dayData!: DaysMealsI;
  item: DaysMealsI | undefined;
  daysMeals: DaysMealsI[] = [] ;
  meals:MealI[] = [];

  isModalOpen = false;
  currentObject :any
  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  constructor(public r : Router
    , private mealGenerationservice:MealGenerationService
    , private errorHandlerService:ErrorHandlerService) {}

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
    console.log('Archive option clicked');
    

  }

  handleSync(meal:string) {
    // Function to handle the "Sync" option action
    console.log('Sync option clicked');
    // Add your custom logic here
    this.mealGenerationservice.handleArchive(this.dayData, meal).subscribe({
      next: (data) => {
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
