import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, map, tap } from 'rxjs';
import { MealI } from '../../models/meal.model';
import { DaysMealsI, FoodItemI, UserI, MealBrowseI } from '../../models/interfaces';
import { title } from 'process';
import { request } from 'http';

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

  getDailyMeals(dayOfWeek : String):Observable<DaysMealsI[]> {
    return this.http.post<any>(
      this.url+'/getDaysMeals',
      { 
        "dayOfWeek" : dayOfWeek.toUpperCase(),
      }
    );
  }

  
  // handleArchive(daysMeals: DaysMealsI, meal: string): Observable<DaysMealsI> {
  //   // const headers = new HttpHeaders({
  //   //   'Content-Type': 'application/json'
  //   // });
  //   return this.http.post<any>(this.url + '/regenerate', daysMeals).pipe(
  //     catchError((error) => {
  //       // Handle errors if the request fails
  //       console.error('Error:', error);
  //       throw error;
  //     }),
  //     map((response) => {
  //       // Return the updated JSON object from the server
  //       return response;
  //     })
  //   );
  // }
  handleArchive(daysMeal: DaysMealsI, meal: String): Observable<DaysMealsI> {
    return this.http.post<any>(
      this.url+'/regenerate',
      {
        "breakfast": daysMeal.breakfast,
        "lunch": daysMeal.lunch,
        "dinner": daysMeal.dinner,
        "mealDate": daysMeal.mealDate.toUpperCase(),
        "meal": meal
      });
  }

  // Helper function to get the headers (if needed)
  private getHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json' // Set the content type of the request
      // Add any other headers if required
    });
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


  getMeal():Observable<MealI> {
    return this.http.get<MealI>(
      this.url+'/getMeal'
    );
  }

  getPopularMeals():Observable<MealBrowseI[]> {
    return this.http.get<MealBrowseI[]>(
      this.url+'/getPopularMeals'
    );
  }

  getSearchedMeals(query: string): Observable<MealBrowseI[]> {
    const params = { query: query }; // backend expects the query parameter
    return this.http.get<MealBrowseI[]>(
      this.url + '/getSearchedMeals', 
      { params: params });
  }

  



}
