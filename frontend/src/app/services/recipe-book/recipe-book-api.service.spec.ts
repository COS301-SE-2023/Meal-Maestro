import { TestBed } from '@angular/core/testing';

import { RecipeBookApiService } from './recipe-book-api.service';
import { HttpClient } from '@angular/common/http';

describe('RecipeBookApiService', () => {
  let service: RecipeBookApiService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    service = new RecipeBookApiService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
