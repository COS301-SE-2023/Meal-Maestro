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
      this.url+'/login',
      {
        "username": user.username,
        "email":user.email,
        "password": user.password
      });
  }

  checkUser()
}
