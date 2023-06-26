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
    return this.http.post<DaysMealsI[]>(
      this.url+'/getDaysMeals',
      {
      "username": this.user.username,
      "email": this.user.email
      }
    );
  }

  getMeal():Observable<MealI> {
    return this.http.post<MealI>(
      this.url+'/getMeal',
      {
      "username": this.user.username,
      "email": this.user.email
      }
    );
  }


}