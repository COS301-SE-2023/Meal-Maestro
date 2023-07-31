import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { DailyMealsComponent } from './daily-meals.component';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';

describe('DailyMealsComponent', () => {
  let component: DailyMealsComponent;
  let fixture: ComponentFixture<DailyMealsComponent>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;

  beforeEach(waitForAsync(() => {
    mockMealGenerationService = jasmine.createSpyObj('MealGenerationService', ['getDailyMeals']);

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), DailyMealsComponent],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DailyMealsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
