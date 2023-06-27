import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SignupPage } from './signup.page';
import { AuthenticationService } from '../../services/services';

describe('SignupPage', () => {
  let component: SignupPage;
  let fixture: ComponentFixture<SignupPage>;
  let mockAuthenicationService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    mockAuthenicationService = jasmine.createSpyObj('AuthenticationService', ['signup', 'checkUser']);

    await TestBed.configureTestingModule({
      imports: [SignupPage],
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
});
