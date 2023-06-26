import { TestBed } from '@angular/core/testing';

import { ImageRetrieverServiceService } from './image-retriever-service.service';

describe('ImageRetrieverServiceService', () => {
  let service: ImageRetrieverServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageRetrieverServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
