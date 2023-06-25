import { of } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';
import { PantryApiService } from './pantry-api.service';
import { HttpClient } from '@angular/common/http';

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

    service.getPantryItems().subscribe({
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

  it('should return item added to pantry', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.addToPantry(expectedItem).subscribe({
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

  it('should remove item from pantry', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.deletePantryItem(expectedItem).subscribe({
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

  it('should update pantry item', (done: DoneFn) => {
    const expectedItem: FoodItemI = {
      "name": "Apple",
      "quantity": 1,
      "weight": 0.1,
    };

    httpClientSpy.post.and.returnValue(of(expectedItem));

    service.updatePantryItem(expectedItem).subscribe({
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
