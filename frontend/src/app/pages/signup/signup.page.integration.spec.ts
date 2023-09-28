import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import {
  AuthenticationService,
  ErrorHandlerService,
} from '../../services/services';
import { SignupPage } from './signup.page';
import { UserI } from '../../models/user.model';
import { TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { Component } from '@angular/core';

describe('SignupPageIntegration', () => {
  let httpMock: HttpTestingController;
  let auth: AuthenticationService;
  let errorHandler: ErrorHandlerService;
  let component: SignupPage;
  let routerSpy = { navigate: jasmine.createSpy('navigate') };
  let apiUrl = 'http://68.183.42.105:8080';
  let mockUser: UserI;
  let mockForm: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        IonicModule.forRoot(),
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([
          { path: 'app/tabs/home', component: DummyComponent },
        ]),
      ],
      providers: [
        AuthenticationService,
        ErrorHandlerService,
        { provide: Router, useValue: routerSpy },
        SignupPage,
      ],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthenticationService);
    errorHandler = TestBed.inject(ErrorHandlerService);
    component = TestBed.inject(SignupPage);

    mockUser = {
      username: 'test',
      password: 'test',
      email: 'test@test.com',
    };

    mockForm = {
      username: 'test',
      initial: 'test',
      verify: 'test',
      email: 'test@test.com',
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should signup a user and navigate to home', async () => {
    spyOn(auth, 'register').and.callThrough();
    spyOn(errorHandler, 'presentSuccessToast').and.callThrough();

    await component.signup(mockForm);

    const req = httpMock.expectOne(apiUrl + '/register');
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'testToken' }, { status: 200, statusText: 'OK' });

    expect(auth.register).toHaveBeenCalledWith(mockUser);
    expect(errorHandler.presentSuccessToast).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['app/tabs/home']);
  });

  it('should display an error if the user entered passwords that do not match', async () => {
    spyOn(errorHandler, 'presentErrorToast').and.callThrough();
    spyOn(auth, 'register').and.callThrough();

    mockForm.verify = 'notTest';

    await component.signup(mockForm);

    expect(errorHandler.presentErrorToast).toHaveBeenCalled();
    expect(auth.register).not.toHaveBeenCalled();
  });

  it('should display an error if the email already exists', async () => {
    spyOn(errorHandler, 'presentErrorToast').and.callThrough();
    spyOn(auth, 'register').and.callThrough();

    await component.signup(mockForm);

    const req = httpMock.expectOne(apiUrl + '/register');
    expect(req.request.method).toBe('POST');
    req.flush(
      { error: 'Email already exists' },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(errorHandler.presentErrorToast).toHaveBeenCalled();
    expect(auth.register).toHaveBeenCalledWith(mockUser);
  });

  it('should display an error if there is a server error', async () => {
    spyOn(errorHandler, 'presentErrorToast').and.callThrough();
    spyOn(auth, 'register').and.callThrough();

    await component.signup(mockForm);

    const req = httpMock.expectOne(apiUrl + '/register');
    expect(req.request.method).toBe('POST');
    req.flush(
      { error: 'Server error' },
      { status: 500, statusText: 'Internal Server Error' }
    );

    expect(errorHandler.presentErrorToast).toHaveBeenCalled();
    expect(auth.register).toHaveBeenCalledWith(mockUser);
  });
});

@Component({ template: '' })
class DummyComponent {}
