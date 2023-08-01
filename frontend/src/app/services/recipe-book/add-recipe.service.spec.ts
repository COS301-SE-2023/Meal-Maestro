import { TestBed } from '@angular/core/testing';

import { AddRecipeService } from './add-recipe.service';

describe('AddRecipeService', () => {
  let service: AddRecipeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddRecipeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
