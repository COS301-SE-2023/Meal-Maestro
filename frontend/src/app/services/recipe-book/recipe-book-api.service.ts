import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserI, RecipeItemI, MealI } from '../../models/interfaces';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecipeBookApiService {

  user: UserI = {
    username: localStorage.getItem('user') ?? '',
    email: localStorage.getItem('email') ?? '',
    password: '', 
  }

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getAllRecipes(): Observable<HttpResponse<MealI[]>> { 
    return this.http.post<MealI[]>(
      this.url+'/getAllRecipes',
      {},
      {observe: 'response'}
    );
  }

  addRecipe(item: MealI): Observable<HttpResponse<MealI>> {
    return this.http.post<MealI>(
      this.url+'/addRecipe',
      {
        "name":item.name,
        "image":item.image
      },
      {observe: 'response'}
    );
  }

  removeRecipe(recipe: MealI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url+'/removeRecipe',
      {        
        "title": recipe.name,
        "image": recipe.image        
      },
      {observe: 'response'}
    );
  }
}
