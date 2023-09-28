import { TestBed } from '@angular/core/testing';

import { LikeDislikeService } from './like-dislike.service';
import { HttpClientModule } from '@angular/common/http';

describe('LikeDislikeService', () => {
  let service: LikeDislikeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule]
    });
    service = TestBed.inject(LikeDislikeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
