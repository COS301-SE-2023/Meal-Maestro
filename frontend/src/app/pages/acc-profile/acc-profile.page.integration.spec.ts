import { TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { AccProfilePage } from './acc-profile.page';
import {
  AuthenticationService,
  ErrorHandlerService,
} from '../../services/services';
import { UserApiService } from '../../services/user-api/user-api.service';
import { UserI } from '../../models/user.model';

describe('AccProfilePageIntegration', () => {
  let component: AccProfilePage;
  let httpMock: HttpTestingController;
  let authService: AuthenticationService;
  let userApiService: UserApiService;
  let errorHandlerService: ErrorHandlerService;
  let user: UserI;
  let apiUrl: string = 'https://68.183.42.105:8080';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), HttpClientTestingModule],
      providers: [AuthenticationService, UserApiService, ErrorHandlerService],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthenticationService);
    userApiService = TestBed.inject(UserApiService);
    errorHandlerService = TestBed.inject(ErrorHandlerService);
    component = TestBed.createComponent(AccProfilePage).componentInstance;

    user = {
      username: 'test',
      email: 'test@test.com',
      password: 'test',
      //   name: 'test',
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getUserInfo on ionViewWillEnter', async () => {
    spyOn(component, 'getUserInfo');
    component.ionViewWillEnter();
    expect(component.getUserInfo).toHaveBeenCalled();
  });

  it('should fetch user Info on getUserInfo', async () => {
    spyOn(authService, 'getUser').and.callThrough();
    await component.getUserInfo();

    const req = httpMock.expectOne(apiUrl + '/getUser');
    expect(req.request.method).toBe('GET');
    req.flush(user);

    component.user = user;

    expect(authService.getUser).toHaveBeenCalled();
    expect(component.user).toEqual(user);
  });

  it('should handle 403 error when getting info', async () => {
    spyOn(authService, 'getUser').and.callThrough();
    spyOn(errorHandlerService, 'presentErrorToast').and.callThrough();
    await component.getUserInfo();

    const req = httpMock.expectOne(apiUrl + '/getUser');
    expect(req.request.method).toBe('GET');
    req.flush('403 error', {
      status: 403,
      statusText: 'Forbidden',
    });

    expect(authService.getUser).toHaveBeenCalled();
    expect(errorHandlerService.presentErrorToast).toHaveBeenCalled();
  });
});
