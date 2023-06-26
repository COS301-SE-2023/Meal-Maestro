import { TestBed } from '@angular/core/testing';

import { MealGenerationService } from './meal-generation.service';

describe('MealGenerationService', () => {
  let service: MealGenerationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MealGenerationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
