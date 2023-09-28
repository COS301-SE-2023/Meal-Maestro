import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SettingsI } from '../../models/settings.model';

@Injectable({
  providedIn: 'root',
})
export class SettingsApiService {
  url: string = 'https://68.183.42.105:8080';

  constructor(private http: HttpClient) {}

  getSettings(): Observable<HttpResponse<SettingsI>> {
    return this.http.post<SettingsI>(
      `${this.url}/getSettings`,
      {},
      { observe: 'response' }
    );
  }

  updateSettings(settings: SettingsI): Observable<HttpResponse<void>> {
    return this.http.post<void>(`${this.url}/updateSettings`, settings, {
      observe: 'response',
    });
  }
}
