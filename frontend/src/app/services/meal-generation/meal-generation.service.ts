import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, concatMap, forkJoin, from, map, switchMap, tap } from 'rxjs';
import { MealI } from '../../models/meal.model';
import { DaysMealsI, FoodItemI, UserI } from '../../models/interfaces';
import { title } from 'process';

@Injectable({
  providedIn: 'root'
})
export class MealGenerationService {

  user: UserI = {
    username: localStorage.getItem('user') ?? '',
    email: localStorage.getItem('email') ?? '',
    password: '', 
  }



  url : String = 'http://localhost:8080';
  constructor(private http: HttpClient) { }

  getDailyMeals():Observable<DaysMealsI[]> {
    return this.http.get<DaysMealsI[]>(
      this.url+'/getDaysMeals')
    // .pipe(
    //   // Adjust the property name according to the actual response structure
    //   map((daysMeals: DaysMealsI[]) => {
    //     return this.retrieveImageUrls(daysMeals).pipe(
    //       map((updatedUrls: string[]) => this.updateMealUrls(daysMeals, updatedUrls))
    //     );
    //   })
    // );
  }


  getMeal():Observable<MealI> {
    return this.http.get<MealI>(
      this.url+'/getMeal'
    );
  }
  // private retrieveImageUrls(daysMeals: DaysMealsI[]): Observable<string[]> {
  //   const imageRequests: Observable<string>[] = [];

  //   for (const dayMeal of daysMeals) {
  //     imageRequests.push(from(this.imageRetriever.getImageUrl(dayMeal.breakfast.name)));
  //     imageRequests.push(from(this.imageRetriever.getImageUrl(dayMeal.lunch.name)));
  //     imageRequests.push(from(this.imageRetriever.getImageUrl(dayMeal.dinner.name)));
  //   }

  //   return forkJoin(imageRequests);
  // }


  // private updateMealUrls(originalMeals: DaysMealsI[], updatedUrls: string[]): DaysMealsI[] {
  //   let index = 0;

  //   return originalMeals.map((dayMeal: DaysMealsI) => ({
  //     ...dayMeal,
  //     breakfast: {
  //       ...dayMeal.breakfast,
  //       url: updatedUrls[index++]
  //     },
  //     lunch: {
  //       ...dayMeal.lunch,
  //       url: updatedUrls[index++]
  //     },
  //     dinner: {
  //       ...dayMeal.dinner,
  //       url: updatedUrls[index++]
  //     }
  //   }));
  // }


}
