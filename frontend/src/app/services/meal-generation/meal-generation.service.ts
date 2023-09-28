import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MealI, RegenerateMealRequestI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root',
})
export class MealGenerationService {
  url: string = 'https://68.183.42.105:8080';

  constructor(private http: HttpClient) {}

  getDailyMeals(date: Date): Observable<HttpResponse<MealI[]>> {
    return this.http.post<MealI[]>(
      this.url + '/getMealPlanForDay',
      {
        date: date.toISOString().split('T')[0],
      },
      { observe: 'response' }
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
  regenerate(request: RegenerateMealRequestI): Observable<HttpResponse<MealI>> {
    return this.http.post<MealI>(
      this.url + '/regenerate',
      {
        meal: request.meal,
        date: request.mealDate?.toISOString().split('T')[0],
      },
      {
        observe: 'response',
      }
    );
  }

  // Helper function to get the headers (if needed)
  private getHeaders() {
    return new HttpHeaders({
      'Content-Type': 'application/json', // Set the content type of the request
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

  getMeal(): Observable<MealI> {
    return this.http.get<MealI>(this.url + '/getMeal');
  }

  getPopularMeals(): Observable<MealI[]> {
    return this.http.get<MealI[]>(
      this.url + '/getPopularMeals'
      // {},
      // {observe: 'response'}
    );
  }

  getSearchedMeals(query: string): Observable<MealI[]> {
    const params = { query: query }; // backend expects the query parameter
    return this.http.get<MealI[]>(this.url + '/getSearchedMeals', {
      params: params,
    });
  }
}
