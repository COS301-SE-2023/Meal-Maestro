import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PantryApiService } from 'frontend/src/app/services/pantry-api/pantry-api.service';

describe('PantryApiService (Integration Test)', () => {
  let service: PantryApiService;
  let httpMock: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PantryApiService]
    }).compileComponents();
  }));

  beforeEach(() => {
    service = TestBed.inject(PantryApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve pantry items from the server', () => {
    const mockPantryItems = [
      { name: 'Item 1', quantity: 10, weight: 100 },
      { name: 'Item 2', quantity: 5, weight: 50 }
    ];

    service.getPantryItems().subscribe((pantryItems) => {
      expect(pantryItems).toEqual(mockPantryItems);
    });

    const req = httpMock.expectOne('http://localhost:8080/getPantry');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      username: 'Frank',
      email: 'test@example.com'
    });

    req.flush(mockPantryItems);
  });

  // More integration test cases...

});
