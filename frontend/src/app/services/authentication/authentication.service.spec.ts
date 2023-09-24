import { of } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { UserI } from '../../models/interfaces';
import { AuthResponseI } from '../../models/authResponse.model';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;
  let routerSpy: jasmine.SpyObj<any>;
  let loginServiceSpy: jasmine.SpyObj<any>;
  let mockUser: UserI;
  let mockAuthResponse: AuthResponseI;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    service = new AuthenticationService(
      httpClientSpy as any,
      routerSpy as any,
      loginServiceSpy as any
    );

    mockUser = {
      username: 'test',
      email: 'test@example.com',
      password: 'test',
    };

    mockAuthResponse = {
      token: 'test',
    };
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login user', (done: DoneFn) => {
    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: mockAuthResponse }))
    );

    service.login(mockUser).subscribe({
      next: (response) => {
        expect(response.body)
          .withContext('expected response')
          .toEqual(mockAuthResponse);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should register user', (done: DoneFn) => {
    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: mockAuthResponse }))
    );

    service.register(mockUser).subscribe({
      next: (response) => {
        expect(response.body)
          .withContext('expected response')
          .toEqual(mockAuthResponse);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should find user', (done: DoneFn) => {
    httpClientSpy.post.and.returnValue(
      of(new HttpResponse({ body: mockUser }))
    );

    service.findUser(mockUser.email).subscribe({
      next: (response) => {
        expect(response.body)
          .withContext('expected response')
          .toEqual(mockUser);
        done();
      },
      error: done.fail,
    });

    expect(httpClientSpy.post.calls.count()).withContext('one call').toBe(1);
  });

  it('should set token', () => {
    const token = 'test';
    service.setToken(token);

    expect(localStorage.getItem('token'))
      .withContext('token set')
      .toEqual(token);
  });
});
