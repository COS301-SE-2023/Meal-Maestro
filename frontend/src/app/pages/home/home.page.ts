import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
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
  isLoading: boolean = true;
  showLoading: boolean = true;
  constructor(
    public r: Router,
    private renderer: Renderer2,
    private el: ElementRef,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService
  ) {}

  async ngOnInit() {
    fetch('assets/burger.svg')
      .then((response) => response.text())
      .then((svg) => {
        this.renderer.setProperty(
          this.el.nativeElement.querySelector('#svg-container'),
          'innerHTML',
          svg
        );
        return fetch('assets/burger.js');
      })
      .then((response) => response.text())
      .then((js) => {
        const script = this.renderer.createElement('script');
        script.type = 'text/javascript';
        script.innerHTML = js;
        this.renderer.appendChild(
          this.el.nativeElement.querySelector('#svg-container'),
          script
        );
      });

    await this.getMeals();
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

  async getMeals() {
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
                mealDay: undefined,
                mealDate: undefined,
              };
              this.hideLoading();
              mealsForDay.breakfast = data.body[0];
              mealsForDay.lunch = data.body[1];
              mealsForDay.dinner = data.body[2];
              mealsForDay.mealDay = this.getDayOfWeek(index);
              mealsForDay.mealDate = date;
              this.daysMeals.push(mealsForDay);
              resolve();
            }
          },
          error: (err) => {
            this.errorHandlerService.presentErrorToast(
              'Error loading meal items',
              err
            );
            this.hideLoading();
            reject();
          },
        });
      });
      date = this.addDays(date, 1);
    }
    console.log(this.daysMeals);
  }

  hideLoading() {
    this.showLoading = false;

    setTimeout(() => {
      this.showLoading = false;
    }, 200);
  }
}
