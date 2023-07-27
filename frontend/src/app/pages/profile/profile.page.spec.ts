import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { ProfilePage } from './profile.page';
import { AuthenticationService } from '../../services/services';
import { UserI } from '../../models/user.model';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

describe('ProfilePage', () => {
  let component: ProfilePage;
  let fixture: ComponentFixture<ProfilePage>;
  let mockAuthService: jasmine.SpyObj<AuthenticationService>;
  let mockUser: UserI;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('AuthenticationService', ['getUser']);

    mockUser = {
      username: "test",
      email: "test@test.com",
      password: "secret"
    }

    const response = new HttpResponse<UserI>({ body: mockUser, status: 200 });
    mockAuthService.getUser.and.returnValue(of(response));

    await TestBed.configureTestingModule({
      imports: [ProfilePage, IonicModule],
      providers: [
        { provide: AuthenticationService, useValue: mockAuthService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
