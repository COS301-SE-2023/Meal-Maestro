import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';


@Injectable({
  providedIn: 'root'
})
export class PantryApiService {

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getPantryItems(): Observable<FoodItemI[]> {
    return this.http.post<FoodItemI[]>(
      this.url+'/getPantry',
      {
      "username": "Frank",
      "email": "test@example.com"
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
          "username": "Frank",
          "email": "test@example.com"
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
          "username": "Frank",
          "email": "test@example.com"
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
          "username": "Frank",
          "email": "test@example.com"
        }
      });
  }
  
}