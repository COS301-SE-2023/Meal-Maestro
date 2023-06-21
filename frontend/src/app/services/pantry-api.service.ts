import { Injectable } from '@angular/core';
import { FoodItem } from '../models/fooditem.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PantryApiService {

  constructor(private http: HttpClient) { }

  getPantryItems(): Observable<FoodItem[]> {
    return this.http.get<FoodItem[]>('https://catfact.ninja/facts?limit=2');
  }
}
