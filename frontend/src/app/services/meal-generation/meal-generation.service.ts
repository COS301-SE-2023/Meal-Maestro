import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
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
      this.url+'/getDaysMeals'
    );
  }

  getMeal():Observable<MealI> {
    return this.http.get<MealI>(
      this.url+'/getMeal'
    );
  }


}
