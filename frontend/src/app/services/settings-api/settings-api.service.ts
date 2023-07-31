import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserPreferencesI } from '../../models/userpreference.model';

@Injectable({
  providedIn: 'root'
})
export class SettingsApiService {

  url: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getSettings(): Observable<HttpResponse<UserPreferencesI>> {
    return this.http.post<UserPreferencesI>(
      `${this.url}/getSettings`,
      {},
      { observe: 'response' }
    );
  }

  updateSettings(settings: UserPreferencesI): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      `${this.url}/updateSettings`,
      settings,
      { observe: 'response' }
    );
  }




}
