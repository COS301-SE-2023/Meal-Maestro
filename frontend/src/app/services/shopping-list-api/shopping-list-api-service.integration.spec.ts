import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ShoppingListApiService } from './shopping-list-api.service';

describe('ShoppingListApiService (Integration Test)', () => {
  let service: ShoppingListApiService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ShoppingListApiService]
    }).compileComponents();
  }));

  beforeEach(() => {
    service = TestBed.inject(ShoppingListApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve shopping list items from the server', () => {
    const mockShoppingListItems = [
      { name: 'Apple', quantity: 1, weight: 0.1 },
      { name: 'Banana', quantity: 2, weight: 0.2 },
      { name: 'Orange', quantity: 3, weight: 0.3 }
    ];

    service.getShoppingListItems().subscribe((shoppingListItems) => {
      expect(shoppingListItems).toEqual(mockShoppingListItems);
    });

    const req = httpMock.expectOne('http://localhost:8080/getShoppingList');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      username: 'Frank',
      email: 'test@example.com'
    });

    req.flush(mockShoppingListItems);
  });

  // More integration test cases...

});
