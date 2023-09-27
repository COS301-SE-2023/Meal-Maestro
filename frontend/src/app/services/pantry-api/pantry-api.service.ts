import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root',
})
export class PantryApiService {
  url: string = 'http://68.183.42.105:8080';

  constructor(private http: HttpClient) {}

  getPantryItems(): Observable<HttpResponse<FoodItemI[]>> {
    return this.http.post<FoodItemI[]>(
      this.url + '/getPantry',
      {},
      { observe: 'response' }
    );
  }

  addToPantry(item: FoodItemI): Observable<HttpResponse<FoodItemI>> {
    return this.http.post<FoodItemI>(
      this.url + '/addToPantry',
      {
        name: item.name,
        quantity: item.quantity,
        unit: item.unit,
        price: item.price,
      },
      { observe: 'response' }
    );
  }

  updatePantryItem(item: FoodItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url + '/updatePantry',
      {
        name: item.name,
        quantity: item.quantity,
        unit: item.unit,
        id: item.id,
      },
      { observe: 'response' }
    );
  }

  deletePantryItem(item: FoodItemI): Observable<HttpResponse<void>> {
    console.log(item);
    return this.http.post<void>(
      this.url + '/removeFromPantry',
      {
        name: item.name,
        quantity: item.quantity,
        unit: item.unit,
        id: item.id,
      },
      { observe: 'response' }
    );
  }
}
