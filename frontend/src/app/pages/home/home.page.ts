import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';
import { DaysMealsI } from '../../models/daysMeals.model';
import { Router } from '@angular/router';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { ErrorHandlerService } from '../../services/services';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule, DailyMealsComponent, CommonModule],
})
export class HomePage implements OnInit {
  daysMeals: DaysMealsI[] = [];
  constructor(
    public r: Router,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService
  ) {}

  async ngOnInit() {
    let date = new Date();
    for (let index = 0; index < 3; index++) {
      await new Promise<void>((resolve, reject) => {
        this.mealGenerationservice.getDailyMeals(date).subscribe({
          next: (data) => {
            if (data.body) {
              let mealsForDay: DaysMealsI = {
                breakfast: undefined,
                lunch: undefined,
                dinner: undefined,
                mealDate: undefined,
              };
              mealsForDay.breakfast = data.body[0];
              mealsForDay.lunch = data.body[1];
              mealsForDay.dinner = data.body[2];
              mealsForDay.mealDate = this.getDayOfWeek(index);
              this.daysMeals.push(mealsForDay);
              resolve();
            }
          },
          error: (err) => {
            this.errorHandlerService.presentErrorToast(
              'Error loading meal items',
              err
            );
            reject();
          },
        });
      });
      date = this.addDays(date, 1);
    }
    console.log(this.daysMeals);
  }

  private getDayOfWeek(dayOffset: number): string {
    const daysOfWeek = [
      'Sunday',
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
    ];
    const today = new Date();
    const targetDate = new Date(today);
    targetDate.setDate(today.getDate() + dayOffset);
    const dayIndex = targetDate.getDay();
    return daysOfWeek[dayIndex];
  }

  private addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }
}
