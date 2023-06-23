import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces.model';


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

}