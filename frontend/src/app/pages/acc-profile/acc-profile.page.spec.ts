import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccProfilePage } from './acc-profile.page';
import { IonicModule, ModalController } from '@ionic/angular';
import {
  AuthenticationService,
  ErrorHandlerService,
} from '../../services/services';
import { HttpResponse } from '@angular/common/http';
import { UserI } from '../../models/user.model';
import { of } from 'rxjs';
import { UserApiService } from '../../services/user-api/user-api.service';

describe('AccProfilePage', () => {
  let component: AccProfilePage;
  let fixture: ComponentFixture<AccProfilePage>;
  let authSpy: jasmine.SpyObj<AuthenticationService>;
  let userApiSpy: jasmine.SpyObj<UserApiService>;
  let errorHandlerSpy: jasmine.SpyObj<ErrorHandlerService>;
  let modalControllerSpy: jasmine.SpyObj<ModalController>;
  let mockUser: UserI;

  beforeEach(async () => {
    authSpy = jasmine.createSpyObj('AuthenticationService', [
      'logout',
      'getUser',
    ]);

    userApiSpy = jasmine.createSpyObj('UserApiService', ['updateUsername']);

    errorHandlerSpy = jasmine.createSpyObj('ErrorHandlerService', [
      'presentErrorToast',
      'presentSuccessToast',
    ]);

    modalControllerSpy = jasmine.createSpyObj('ModalController', [
      'create',
      'dismiss',
    ]);

    mockUser = {
      username: 'test',
      email: 'test@test.com',
      password: 'secret',
    };

    const response = new HttpResponse<UserI>({ body: mockUser, status: 200 });
    authSpy.getUser.and.returnValue(of(response));

    await TestBed.configureTestingModule({
      imports: [AccProfilePage, IonicModule],
      providers: [
        { provide: AuthenticationService, useValue: authSpy },
        { provide: UserApiService, useValue: userApiSpy },
        { provide: ErrorHandlerService, useValue: errorHandlerSpy },
        { provide: ModalController, useValue: modalControllerSpy },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AccProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getUserInfo() on ionViewWillEnter()', () => {
    spyOn(component, 'getUserInfo');
    component.ionViewWillEnter();
    expect(component.getUserInfo).toHaveBeenCalled();
  });
});
