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

  getSettings(token: string): Observable<HttpResponse<UserPreferencesI>> {
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.post<UserPreferencesI>(
      `${this.url}/getSettings`,
      {},
      { headers, observe: 'response' }
    );
  }

  updateSettings(settings: UserPreferencesI, token: string): Observable<HttpResponse<void>> {
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.post<void>(
      `${this.url}/updateSettings`,
      settings,
      { headers, observe: 'response' }
    );
  }


}
