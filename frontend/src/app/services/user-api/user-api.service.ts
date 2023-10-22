import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserI } from '../../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserApiService {
  url: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  updateUsername(user: UserI): Observable<HttpResponse<UserI>> {
    return this.http.put<UserI>(
      this.url + '/updateUser',
      {
        username: user.username,
        email: user.email,
        password: user.password,
      },
      { observe: 'response' }
    );
  }
}
