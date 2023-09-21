import { TestBed } from '@angular/core/testing';

import { LikeDislikeService } from './like-dislike.service';

describe('LikeDislikeService', () => {
  let service: LikeDislikeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LikeDislikeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
