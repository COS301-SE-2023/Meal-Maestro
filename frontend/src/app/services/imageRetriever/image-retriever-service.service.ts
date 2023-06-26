import { HttpClient , HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';

import axios from 'axios';
import { Observable, map } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ImageRetrieverServiceService {
  private apiKey = 'AIzaSyCtlVkqnRcjsDGAwHbKfO85Tqic8D-o4uc'; // Replace with your Google Custom Search API key
 // private cx = '95dcdf9417b3b4831'; // Replace with your Custom Search Engine ID
  private apiUrl = 'https://customsearch.googleapis.com/customsearch/v1';

  constructor(private http: HttpClient) {}

  getImageUrl(mealName: string): Observable<string> {
    const params = new HttpParams()
      .set('key', this.apiKey)
     // .set('cx', this.cx)
      .set('q', mealName)
      .set('searchType', 'image')
      .set('num', '1');

    return this.http.get<any>(this.apiUrl, { params }).pipe(
      map((response) => {
        const items = response?.items;
        if (items && items.length > 0) {
          return items[0].link;
        } else {
          throw new Error('Image not found');
        }
      })
    );
  }
}