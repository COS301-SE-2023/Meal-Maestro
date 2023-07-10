import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  login(user: UserI): Observable<HttpResponse<string>> {
    return this.http.post<string>(
      this.url+'/authenticate',
      {
        "email":user.email,
        "password": user.password
      },
      {observe: 'response'});
  }

  register(user: UserI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      this.url+'/register',
      {
        "username": user.username,
        "email":user.email,
        "password": user.password
      },
      {observe: 'response'});
  }

  findUser(email: string): Observable<HttpResponse<UserI>> {
    return this.http.post<UserI>(
      this.url+'/findByEmail',
      {
        "username": '',
        "email": email,
        "password": ''
      },
      {observe: 'response'});
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }
}
