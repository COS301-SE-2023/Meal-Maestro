import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserI, RecipeItemI } from '../../models/interfaces';
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

  getAllRecipes(): Observable<HttpResponse<RecipeItemI[]>> { 
    return this.http.post<RecipeItemI[]>(
      this.url+'/getAllRecipes',
      {},
      {observe: 'response'}
    );
  }

  addRecipe(item: RecipeItemI): Observable<RecipeItemI> {
    return this.http.post<RecipeItemI>(
      this.url+'/addRecipe',
      {
        "recipe": {
          "title": item.title,
          "image": item.image
        },
        "user": {
          "username": this.user.username,
          "email": this.user.email
        }
      }
    );
  }

  removeRecipe(recipe: RecipeItemI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url+'/removeRecipe',
      {        
        "title": recipe.title,
        "image": recipe.title        
      },
      {observe: 'response'}
    );
  }
}
