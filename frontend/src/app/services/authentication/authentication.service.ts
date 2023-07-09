import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  url : String = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  login(user: UserI): Observable<boolean> {
    return this.http.post<boolean>(
      this.url+'/authenticate',
      {
        "email":user.email,
        "password": user.password
      });
  }

  register(user: UserI): Observable<void> {
    return this.http.post<void>(
      this.url+'/register',
      {
        "username": user.username,
        "email":user.email,
        "password": user.password
      });
  }

  findUser(email: string): Observable<UserI> {
    return this.http.post<UserI>(
      this.url+'/findByEmail',
      {
        "username": '',
        "email": email,
        "password": ''
      });
  }
}
