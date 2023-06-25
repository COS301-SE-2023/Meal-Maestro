import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ShoppingListApiService {

  url: String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getShoppingListItems(): Observable<FoodItemI[]> {
    return this.http.post<FoodItemI[]>(
      this.url + '/getShoppingList',
      {
        "username": "Frank",
        "email": "test@example.com"
      });
  }

  addToShoppingList(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url + '/addToShoppingList',
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

  updateShoppingListItem(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url + '/updateShoppingList',
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

  deleteShoppingListItem(item: FoodItemI): Observable<FoodItemI> {
    return this.http.post<FoodItemI>(
      this.url + '/removeFromShoppingList',
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
