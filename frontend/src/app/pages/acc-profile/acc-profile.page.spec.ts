import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccProfilePage } from './acc-profile.page';
import { IonicModule } from '@ionic/angular';
import { AuthenticationService } from '../../services/services';
import { HttpResponse } from '@angular/common/http';
import { UserI } from '../../models/user.model';
import { of } from 'rxjs';
import { UserApiService } from '../../services/user-api/user-api.service';

describe('AccProfilePage', () => {
  let component: AccProfilePage;
  let fixture: ComponentFixture<AccProfilePage>;
  let mockAuthService: jasmine.SpyObj<AuthenticationService>;
  let mockUserApiService: jasmine.SpyObj<UserApiService>;
  let mockUser: UserI;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('AuthenticationService', [
      'logout',
      'getUser',
    ]);

    mockUserApiService = jasmine.createSpyObj('UserApiService', [
      'updateUsername',
    ]);

    mockUser = {
      username: 'test',
      email: 'test@test.com',
      password: 'secret',
    };

    const response = new HttpResponse<UserI>({ body: mockUser, status: 200 });
    mockAuthService.getUser.and.returnValue(of(response));

    await TestBed.configureTestingModule({
      imports: [AccProfilePage, IonicModule],
      providers: [
        { provide: AuthenticationService, useValue: mockAuthService },
        { provide: UserApiService, useValue: mockUserApiService },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AccProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
