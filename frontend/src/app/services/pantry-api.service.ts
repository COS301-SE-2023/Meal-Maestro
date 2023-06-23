import { Injectable } from '@angular/core';
import { FoodItemI } from '../models/fooditem.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PantryApiService {

  constructor(private http: HttpClient) { }

  getPantryItems(): Observable<FoodItemI[]> {
    return this.http.get<FoodItemI[]>('https://catfact.ninja/facts?limit=2');
  }
}