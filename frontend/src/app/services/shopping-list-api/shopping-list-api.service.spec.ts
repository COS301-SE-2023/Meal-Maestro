import { ShoppingListApiService } from './shopping-list-api.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { FoodItemI } from '../../models/interfaces';
import { of } from 'rxjs';

describe('ShoppingListApiService', () => {
  let service: ShoppingListApiService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    service = new ShoppingListApiService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return shopping list items', (done: DoneFn) => {
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

    service.getShoppingListItems().subscribe({
      next: (res) => {
        expect(res.body).withContext('expected items').toEqual(expectedItems);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should return item added to shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: expectedItem }))
    );

    service.addToShoppingList(expectedItem).subscribe({
      next: (res) => {
        expect(res.body).withContext('expected item').toEqual(expectedItem);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should update item in shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(of(new HttpResponse({ status: 200 })));

    service.updateShoppingListItem(expectedItem).subscribe({
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

  it('should delete item from shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      name: 'Apple',
      quantity: 1,
      unit: 'pcs',
    };

    httpClientSpy.post.and.returnValue(of(new HttpResponse({ status: 200 })));

    service.deleteShoppingListItem(expectedItem).subscribe({
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
