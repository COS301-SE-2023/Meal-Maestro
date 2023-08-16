import { of } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';
import { PantryApiService } from './pantry-api.service';
import { HttpClient, HttpResponse } from '@angular/common/http';

describe('PantryApiService', () => {
  let service: PantryApiService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    service = new PantryApiService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return pantry items', (done: DoneFn) => {
    const expectedItems: FoodItemI[] = [
      {
        name: 'Apple',
        quantity: 1,
        unit: 'pcs',
      },
      {
        name: 'Banana',
        quantity: 2,
        unit: 'pcs',
      },
      {
        name: 'Orange',
        quantity: 3,
        unit: 'pcs',
      },
    ];

    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: expectedItems }))
    );

    service.getPantryItems().subscribe({
      next: (res) => {
        expect(res.body).withContext('expected items').toEqual(expectedItems);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should return item added to pantry', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: expectedItem }))
    );

    service.addToPantry(expectedItem).subscribe({
      next: (res) => {
        expect(res.body).withContext('expected item').toEqual(expectedItem);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should remove item from pantry', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(of(new HttpResponse({ status: 200 })));

    service.deletePantryItem(expectedItem).subscribe({
      next: (res) => {
        expect(res.status)
          .withContext('expected HTTP status code 200')
          .toEqual(200);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should update pantry item', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(of(new HttpResponse({ status: 200 })));

    service.updatePantryItem(expectedItem).subscribe({
      next: (res) => {
        expect(res.status)
          .withContext('expected HTTP status code 200')
          .toEqual(200);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });
});
