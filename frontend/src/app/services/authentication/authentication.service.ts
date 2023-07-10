import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserI } from '../../models/interfaces';
import { AuthResponseI } from '../../models/authResponse.model';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  login(user: UserI): Observable<HttpResponse<AuthResponseI>> {
    return this.http.post<AuthResponseI>(
      this.url+'/authenticate',
      {
        "email":user.email,
        "password": user.password
      },
      {observe: 'response'});
  }

  register(user: UserI): Observable<HttpResponse<AuthResponseI>> {
    return this.http.post<AuthResponseI>(
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
    if (token){
      localStorage.setItem('token', token);
    }
  }
}
