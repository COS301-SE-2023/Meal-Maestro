import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SignupPage } from './signup.page';
import { AuthenticationService } from '../../services/services';
import { AuthResponseI } from '../../models/authResponse.model';
import { HttpResponse } from '@angular/common/http';
import { IonicModule } from '@ionic/angular';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { Component } from '@angular/core';

describe('SignupPage', () => {
  let component: SignupPage;
  let fixture: ComponentFixture<SignupPage>;
  let mockAuthenicationService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    mockAuthenicationService = jasmine.createSpyObj('AuthenticationService', ['register', 'setToken']);

    const response = new HttpResponse<AuthResponseI>({ body: { token: 'test' }, status: 200 });

    mockAuthenicationService.register.and.returnValue(of(response));

    await TestBed.configureTestingModule({
      imports: [SignupPage, IonicModule, RouterTestingModule.withRoutes([
        {path: 'app/tabs/home', component: DummyComponent}
      ])
      ],
      providers: [
        { provide: AuthenticationService, useValue: mockAuthenicationService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SignupPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register', () => {
    let mockUser = {
      username: 'test',
      password: 'test',
      email: 'test@test.com'
    };
    component.signup(mockUser);
    expect(mockAuthenicationService.register).toHaveBeenCalled();
  });
});


@Component({template: ''})
class DummyComponent {}