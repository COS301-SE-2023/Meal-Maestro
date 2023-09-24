import { TestBed } from '@angular/core/testing';

import { BarcodeApiService } from './barcode-api.service';
import { HttpClient } from '@angular/common/http';

describe('BarcodeApiService', () => {
  let service: BarcodeApiService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    service = new BarcodeApiService(httpClientSpy as any);
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
