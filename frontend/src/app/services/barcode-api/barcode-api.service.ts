import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';

@Injectable({
  providedIn: 'root',
})
export class BarcodeApiService {
  url: String = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  findProduct(barcode: String): Observable<HttpResponse<FoodItemI>> {
    return this.http.post<FoodItemI>(
      this.url + '/findProduct',
      {
        barcode: barcode,
      },
      { observe: 'response' }
    );
  }
}
