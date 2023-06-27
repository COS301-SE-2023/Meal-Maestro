import { TestBed } from '@angular/core/testing';

import { MealGenerationService } from './meal-generation.service';
import { HttpClient } from '@angular/common/http';

describe('MealGenerationService', () => {
  let service: MealGenerationService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get']);
    service = new MealGenerationService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
