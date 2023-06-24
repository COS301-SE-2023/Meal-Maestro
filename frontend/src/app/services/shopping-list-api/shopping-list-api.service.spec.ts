import { TestBed } from '@angular/core/testing';

import { ShoppingListApiService } from './shopping-list-api.service';

describe('ShoppingListApiService', () => {
  let service: ShoppingListApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShoppingListApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
