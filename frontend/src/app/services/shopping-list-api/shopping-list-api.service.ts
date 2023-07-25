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
      {},
      { observe: 'response' });
  }

  addToShoppingList(item: FoodItemI): Observable<HttpResponse<FoodItemI>> {
    return this.http.post<FoodItemI>(
      this.url + '/addToShoppingList',
      {
        "name": item.name,
        "quantity": item.quantity,
        "weight": item.weight,
      },
      { observe: 'response' });
  }

  updateShoppingListItem(item: FoodItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url + '/updateShoppingList',
      {
        "name": item.name,
        "quantity": item.quantity,
        "weight": item.weight,
      },
      { observe: 'response' });
  }

  deleteShoppingListItem(item: FoodItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url + '/removeFromShoppingList',
      {
        "name": item.name,
        "quantity": item.quantity,
        "weight": item.weight,
      },
      { observe: 'response' });
  }

  buyItem(item: FoodItemI): Observable<HttpResponse<FoodItemI[]>> {
    return this.http.post<FoodItemI[]>(
      this.url + '/buyItem',
      {
        "name": item.name,
        "quantity": item.quantity,
        "weight": item.weight,
      },
      { observe: 'response' });
  }
}
