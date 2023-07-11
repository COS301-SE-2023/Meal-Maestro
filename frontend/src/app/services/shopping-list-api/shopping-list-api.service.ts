import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ShoppingListApiService {

  url: String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getShoppingListItems(): Observable<HttpResponse<FoodItemI[]>> {
    return this.http.post<FoodItemI[]>(
      this.url + '/getShoppingList',
      {
        "token": localStorage.getItem('token')
      },
      { observe: 'response' });
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
