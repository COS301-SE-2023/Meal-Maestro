import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FoodItemI, UserI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ShoppingListApiService {

  user: UserI = {
    username: localStorage.getItem('user') ?? '',
    email: localStorage.getItem('email') ?? '',
    password: '', 
  }

  url: String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getShoppingListItems(): Observable<FoodItemI[]> {
    return this.http.post<FoodItemI[]>(
      this.url + '/getShoppingList',
      {
        "username": this.user.username,
        "email": this.user.email
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
          "username": this.user.username,
          "email": this.user.email
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
          "username": this.user.username,
          "email": this.user.email
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
          "username": this.user.username,
          "email": this.user.email
        }
      });
  }
}
