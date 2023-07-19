import { TestBed } from '@angular/core/testing';

import { RecipeBookApiService } from './recipe-book-api.service';

describe('RecipeBookApiService', () => {
  let service: RecipeBookApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecipeBookApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
