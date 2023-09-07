import { TestBed } from '@angular/core/testing';

import { BarcodeApiService } from './barcode-api.service';

describe('BarcodeApiService', () => {
  let service: BarcodeApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BarcodeApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
