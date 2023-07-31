import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginPage } from './login.page';
import { IonicModule } from '@ionic/angular';
import { AuthenticationService } from '../../services/services';
import { UserI } from '../../models/user.model';
import { HttpResponse } from '@angular/common/http';
import { AuthResponseI } from '../../models/authResponse.model';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';

describe('LoginPage', () => {
  let component: LoginPage;
  let fixture: ComponentFixture<LoginPage>;
  let mockAuthenicationService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    mockAuthenicationService = jasmine.createSpyObj('AuthenticationService', ['login', 'setToken']);

    const response = new HttpResponse<AuthResponseI>({ body: { token: 'test' }, status: 200 });

    mockAuthenicationService.login.and.returnValue(of(response));

    await TestBed.configureTestingModule({
      imports: [LoginPage, IonicModule, RouterTestingModule.withRoutes([
        {path: 'app/tabs/home', component: DummyComponent}
      ])],
      providers: [
        { provide: AuthenticationService, useValue: mockAuthenicationService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginPage);
    component = fixture.componentInstance;
    fixture.detectChanges();


  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call login', () => {
    let mockUser : UserI = {
      username: 'test',
      password: 'test',
      email: 'test@test.com',
    }

    component.login(mockUser);
    expect(mockAuthenicationService.login).toHaveBeenCalled();

  });
});

@Component({template: ''})
class DummyComponent {}