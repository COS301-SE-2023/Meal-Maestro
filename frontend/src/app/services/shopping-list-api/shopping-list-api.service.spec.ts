import { ShoppingListApiService } from './shopping-list-api.service';
import { HttpClient } from '@angular/common/http';
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
          "name": "Apple",
          "quantity": 1,
          "weight": 0.1,
      },
      {
          "name": "Banana",
          "quantity": 2,
          "weight": 0.2,
      },
      {
          "name": "Orange",
          "quantity": 3,
          "weight": 0.3,
      }
    ];

    httpClientSpy.post.and.returnValue(of(expectedItems));

    service.getShoppingListItems().subscribe({
      next: items => {
        expect(items)
          .withContext('expected items')
          .toEqual(expectedItems);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

  it('should return item added to shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.addToShoppingList(expectedItem).subscribe({
      next: item => {
        expect(item)
          .withContext('expected item')
          .toEqual(expectedItem);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

  it('should update item in shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.updateShoppingListItem(expectedItem).subscribe({
      next: item => {
        expect(item)
          .withContext('expected item')
          .toEqual(expectedItem);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

  it('should delete item from shopping list', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.deleteShoppingListItem(expectedItem).subscribe({
      next: item => {
        expect(item)
          .withContext('expected item')
          .toEqual(expectedItem);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

});
