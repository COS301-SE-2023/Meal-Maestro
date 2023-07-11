import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';


@Injectable({
  providedIn: 'root'
})
export class PantryApiService {

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getPantryItems(): Observable<HttpResponse<FoodItemI[]>> {
    return this.http.post<FoodItemI[]>(
      this.url+'/getPantry',
      {
        "token": localStorage.getItem('token')
      },
      {observe: 'response'});
  }

  addToPantry(item: FoodItemI): Observable<HttpResponse<FoodItemI>> {
    return this.http.post<FoodItemI>(
      this.url+'/addToPantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "token": localStorage.getItem('token')
      },
      {observe: 'response'});
  }

  updatePantryItem(item: FoodItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url+'/updatePantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "token": localStorage.getItem('token')
      },
      {observe: 'response'});
  }

  deletePantryItem(item: FoodItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url+'/removeFromPantry',
      {
        "food": {
          "name": item.name,
          "quantity": item.quantity,
          "weight": item.weight,
        },
        "token": localStorage.getItem('token')
      },
      {observe: 'response'});
  }
  
}