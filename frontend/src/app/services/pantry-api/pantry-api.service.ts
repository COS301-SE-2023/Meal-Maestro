import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI, UserI } from '../../models/interfaces';


@Injectable({
  providedIn: 'root'
})
export class PantryApiService {

  user: UserI = {
    username: localStorage.getItem('user') ?? '',
    email: localStorage.getItem('email') ?? '',
    password: '', 
  }

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getPantryItems(): Observable<FoodItemI[]> {
    return this.http.post<FoodItemI[]>(
      this.url+'/getPantry',
      {
      "username": this.user.username,
      "email": this.user.email
      });
  }

  addToPantry(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url+'/addToPantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "user": {
          "username": this.user.username,
          "email": this.user.email
        }
      });
  }

  updatePantryItem(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url+'/updatePantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "user": {
          "username": this.user.username,
          "email": this.user.email
        }
      });
  }

  deletePantryItem(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url+'/removeFromPantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "user": {
          "username": this.user.username,
          "email": this.user.email
        }
      });
  }
  
}