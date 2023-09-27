import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root',
})
export class BarcodeApiService {
  url: string = 'http://68.183.42.105:8080';

  constructor(private http: HttpClient) {}

  findProduct(
    barcode: string,
    store: string
  ): Observable<HttpResponse<FoodItemI>> {
    return this.http.post<FoodItemI>(
      this.url + '/findProduct',
      {
        barcode: barcode,
        store: store,
      },
      { observe: 'response' }
    );
  }
}
