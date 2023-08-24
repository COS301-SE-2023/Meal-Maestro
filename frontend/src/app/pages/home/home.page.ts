import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';
import { DaysMealsI } from '../../models/daysMeals.model';
import { Router } from '@angular/router';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { ErrorHandlerService } from '../../services/services';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

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
    for (let index = 0; index < 4; index++) {
      this.mealGenerationservice
        .getDailyMeals(this.getDayOfWeek(index))
        .subscribe({
          next: (data: DaysMealsI[] | DaysMealsI) => {
            if (Array.isArray(data)) {
              const mealsWithDate = data.map((item) => ({
                ...item,
                mealDate: this.getDayOfWeek(index),
              }));
              this.daysMeals.push(...mealsWithDate);
            } else {
              data.mealDate = this.getDayOfWeek(index);
              this.daysMeals.push(data);
            }
          },
          error: (err) => {
            this.errorHandlerService.presentErrorToast(
              'Error loading meal items',
              err
            );
          },
        });

      const observables = [];

      for (let index = 0; index < 4; index++) {
        const observable = this.mealGenerationservice.getDailyMeals(
          this.getDayOfWeek(index)
        );
        observables.push(observable);
      }

      forkJoin(observables).subscribe({
        next: (dataArray: (DaysMealsI[] | DaysMealsI)[]) => {
          dataArray.forEach((data, index) => {
            if (Array.isArray(data)) {
              const mealsWithDate = data.map((item) => ({
                ...item,
                mealDate: this.getDayOfWeek(index),
              }));
              this.daysMeals.push(...mealsWithDate);
            } else {
              data.mealDate = this.getDayOfWeek(index);
              this.daysMeals.push(data);
            }
          });
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error loading meal items',
            err
          );
        },
      });
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
