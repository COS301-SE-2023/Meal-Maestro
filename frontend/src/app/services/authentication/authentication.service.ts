import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserI } from '../../models/interfaces';
import { AuthResponseI } from '../../models/authResponse.model';
import { Router } from '@angular/router';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  url: string = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    public r: Router,
    private l: LoginService
  ) {}

  login(user: UserI): Observable<HttpResponse<AuthResponseI>> {
    return this.http.post<AuthResponseI>(
      this.url + '/authenticate',
      {
        email: user.email,
        password: user.password,
      },
      { observe: 'response' }
    );
  }

  register(user: UserI): Observable<HttpResponse<AuthResponseI>> {
    return this.http.post<AuthResponseI>(
      this.url + '/register',
      {
        username: user.username,
        email: user.email,
        password: user.password,
      },
      { observe: 'response' }
    );
  }

  findUser(email: string): Observable<HttpResponse<UserI>> {
    return this.http.post<UserI>(
      this.url + '/findByEmail',
      {
        username: '',
        email: email,
        password: '',
      },
      { observe: 'response' }
    );
  }

  updateUser(user: UserI): Observable<HttpResponse<UserI>> {
    return this.http.post<UserI>(
      this.url + '/updateUser',
      {
        username: user.username,
        email: '',
        password: '',
      },
      { observe: 'response' }
    );
  }

  getUser(): Observable<HttpResponse<UserI>> {
    return this.http.get<UserI>(this.url + '/getUser', { observe: 'response' });
  }

  setToken(token: string): void {
    if (token) {
      localStorage.setItem('token', token);
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.r.navigate(['../']);
    this.l.resetRefreshed();
  }
}
