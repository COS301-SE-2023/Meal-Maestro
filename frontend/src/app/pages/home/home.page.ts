import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';
import { DaysMealsI } from '../../models/daysMeals.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule, DailyMealsComponent, CommonModule],
})
export class HomePage implements OnInit{
  daysMeals: DaysMealsI[] = [];
   constructor(public r : Router
    , private mealGenerationservice:MealGenerationService
    , private errorHandlerService:ErrorHandlerService) {};

  async ngOnInit() {
   
    //  for (let index = 0; index < 4; index++) {
    //   this.mealGenerationservice.getDailyMeals(this.getDayOfWeek(index)).subscribe({
    //     next: (data: DaysMealsI[] | DaysMealsI) => {
    //       if (Array.isArray(data)) {
    //         const mealsWithDate = data.map((item) => ({
    //           ...item,
    //           mealDate: this.getDayOfWeek(index),
    //         }));
    //         this.daysMeals.push(...mealsWithDate);
    //       } else {
    //         data.mealDate = this.getDayOfWeek(index);
    //         this.daysMeals.push(data);
    //       }
          
    //     },
    //     error: (err) => {
    //       this.errorHandlerService.presentErrorToast(
    //         'Error loading meal items',
    //         err
    //       );
    //     },
    //   });

    const observables = [];
   
    for (let index = 0; index < 4; index++) {
      const observable = this.mealGenerationservice.getDailyMeals(this.getDayOfWeek(index));
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
        this.errorHandlerService.presentErrorToast('Error loading meal items', err);
      },
    });
    }

  
  private getDayOfWeek(dayOffset: number): string {
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
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




  // todayArray: {identifier:string, title: string, description: string, url: string, ingredients: string, instructions: string, cookingTime:string }[] = [
  //   {identifier:"Breakfast", title: "Muesli & Yogurt", description: " A classic Nutritious breakfast with a low gi to sustain you through your busy day", url: "https://www.jessicagavin.com/wp-content/uploads/2016/06/berry-muesli-breakfast-bowls.jpg", ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :" },
  //   {identifier:"Lunch", title: "Chicken Alfredo", description: "A savoury filling lunch.", url: "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :" },
  //  // {identifier:"Breakfast", title: "Fried Chicken Tenders", description: "Crispy and golden-brown on the outside, tender and juicy on the inside, the classic comfort food favorite", url: "https://images.unsplash.com/photo-1614398751058-eb2e0bf63e53?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1014&q=80", recipe: "RECIPE OF : Fried Chicken Tenders" },
  //   {identifier:"Dinner", title: "Butternut soup", description: "A warm soup to fill you in these winter months.", url: "https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80", ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :" },
  // //  {identifier:"Lunch", title: "Greek Salad with Chicken", description: "A light and refreshing salad featuring a flavorful and protein-packed option for a healthy meal prep.", url: "https://images.unsplash.com/photo-1580013759032-c96505e24c1f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1818&q=80", recipe: "RECIPE OF : Greek Salad with Chicken" },
  //  // {identifier:"Dinner", title: "Curry Tofu Stir-Fry", description: "It's a balanced and refreshing option packed with protein and nutrients.", url: "https://images.unsplash.com/photo-1564834724105-918b73d1b9e0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=388&q=80",recipe: "RECIPE OF : Curry Tofu Stir-Fry" },
  //   // Additional entries...
    
  // ];

  // tomorrowArray: {identifier:string, title: string, description: string, url: string,ingredients: string, instructions: string, cookingTime:string }[] = [
  //  // {identifier:"Breakfast", title: "Muesli & Yogurt", description: " A classic Nutritious breakfast with a low gi to sustain you through your busy day", url: "https://www.jessicagavin.com/wp-content/uploads/2016/06/berry-muesli-breakfast-bowls.jpg", recipe: "RECIPE OF : Muesli & Yogurt" },
  // //  {identifier:"Lunch", title: "Chicken Alfredo", description: "A savoury filling lunch.", url: "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",recipe: "RECIPE OF : Chicken Alfredo" },
  //   {identifier:"Breakfast", title: "Fried Chicken Tenders", description: "Crispy and golden-brown on the outside, tender and juicy on the inside, the classic comfort food favorite", url: "https://images.unsplash.com/photo-1614398751058-eb2e0bf63e53?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1014&q=80",ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :"},
  // //  {identifier:"Dinner", title: "Butternut soup", description: "A warm soup to fill you in these winter months.", url: "https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80", recipe: "RECIPE OF : Butternut Soup" },
  //   {identifier:"Lunch", title: "Greek Salad with Chicken", description: "A light and refreshing salad featuring a flavorful and protein-packed option for a healthy meal prep.", url: "https://images.unsplash.com/photo-1580013759032-c96505e24c1f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1818&q=80",ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :" },
  //   {identifier:"Dinner", title: "Curry Tofu Stir-Fry", description: "It's a balanced and refreshing option packed with protein and nutrients.", url: "https://images.unsplash.com/photo-1564834724105-918b73d1b9e0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=388&q=80",ingredients: "INGREDIENTS :", instructions:"INSTRUCTIONS :", cookingTime:"COOKING TIME :" },
  //   // Additional entries...
    
  // ];
  
}import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { ErrorHandlerService } from '../../services/services';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

