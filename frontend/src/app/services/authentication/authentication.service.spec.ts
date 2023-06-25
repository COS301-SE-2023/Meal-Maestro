import { of } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { HttpClient } from '@angular/common/http';
import { UserI } from '../../models/interfaces';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    service = new AuthenticationService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return true if user is able to login', (done: DoneFn) => {
    const mockUser: UserI = {
      "username": "test",
      "email": "test@example.com",
      "password": "test"
    };

    const expectedResponse: boolean = true;

    httpClientSpy.post.and.returnValue(of(expectedResponse));

    service.login(mockUser).subscribe({
      next: response => {
        expect(response)
          .withContext('expected response')
          .toEqual(expectedResponse);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

  it('#checkUser should return true if user exists', (done: DoneFn) => {
    const mockUser: UserI = {
      "username": "test",
      "email": "test@example.com",
      "password": "test"
    };

    const expectedResponse: boolean = true;

    httpClientSpy.post.and.returnValue(of(expectedResponse));

    service.checkUser(mockUser).subscribe({
      next: response => {
        expect(response)
          .withContext('expected response')
          .toEqual(expectedResponse);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

  it('#createUser should return void', (done: DoneFn) => {
    const mockUser: UserI = {
      "username": "test",
      "email": "test@example.com",
      "password": "test"
    };

    const expectedResponse: void = undefined;

    httpClientSpy.post.and.returnValue(of(expectedResponse));

    service.createUser(mockUser).subscribe({
      next: response => {
        expect(response)
          .withContext('expected response')
          .toEqual(expectedResponse);
        done();
      },
      error: done.fail
    });

    expect(httpClientSpy.post.calls.count())
      .withContext('one call')
      .toBe(1);
  });

});
