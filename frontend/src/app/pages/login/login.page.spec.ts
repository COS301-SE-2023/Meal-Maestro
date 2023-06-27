import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginPage } from './login.page';
import { IonicModule } from '@ionic/angular';
import { AuthenticationService } from '../../services/services';

describe('LoginPage', () => {
  let component: LoginPage;
  let fixture: ComponentFixture<LoginPage>;
  let mockAuthenicationService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    mockAuthenicationService = jasmine.createSpyObj('AuthenticationService', ['login']);

    await TestBed.configureTestingModule({
      imports: [LoginPage, IonicModule],
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
});
