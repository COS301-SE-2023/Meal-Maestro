import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MealI } from '../../models/meal.model';

@Injectable({
  providedIn: 'root',
})
export class LikeDislikeService {
  url: String = 'http://68.183.42.105:8080';

  constructor(private http: HttpClient) {}

  liked(item: MealI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url + '/liked',
      {
        name: item.name,
        description: item.description,
        image: item.image,
        ingredients: item.ingredients,
        instructions: item.instructions,
        cookingTime: item.cookingTime,
        type: item.type,
      },
      { observe: 'response' }
    );
  }

  disliked(item: MealI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url + '/disliked',
      {
        name: item.name,
        description: item.description,
        image: item.image,
        ingredients: item.ingredients,
        instructions: item.instructions,
        cookingTime: item.cookingTime,
        type: item.type,
      },
      { observe: 'response' }
    );
  }
}
