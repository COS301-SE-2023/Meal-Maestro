import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';
import { HomePage } from './home.page';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';

describe('HomePage', () => {
  let component: HomePage;
  let fixture: ComponentFixture<HomePage>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;

  beforeEach(async () => {
    // mockMealGenerationService = jasmine.createSpyObj('MealGenerationService', ['generateMeals']);

    await TestBed.configureTestingModule({
      imports: [HomePage, IonicModule, DailyMealsComponent],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService },
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
