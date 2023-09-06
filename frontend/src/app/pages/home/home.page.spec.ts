import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';
import { HomePage } from './home.page';
import {
  AuthenticationService,
  MealGenerationService,
} from '../../services/services';

describe('HomePage', () => {
  let component: HomePage;
  let fixture: ComponentFixture<HomePage>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;
  let mockAuthService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    // mockMealGenerationService = jasmine.createSpyObj('MealGenerationService', ['generateMeals']);
    mockAuthService = jasmine.createSpyObj('AuthenticationService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [HomePage, IonicModule, DailyMealsComponent],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService },
        { provide: AuthenticationService, useValue: mockAuthService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HomePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
