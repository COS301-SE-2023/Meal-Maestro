import { TestBed } from '@angular/core/testing';

import { UserApiService } from './user-api.service';
import { HttpClient } from '@angular/common/http';

describe('UserApiService', () => {
  let service: UserApiService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    service = new UserApiService(httpClientSpy as any);
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
