import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { DailyMealsComponent } from './daily-meals.component';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { DaysMealsI } from '../../models/interfaces';
import { HttpClientModule } from '@angular/common/http';

describe('DailyMealsComponent', () => {
  let component: DailyMealsComponent;
  let fixture: ComponentFixture<DailyMealsComponent>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;
  let mockDaysMeals: DaysMealsI;

  beforeEach(waitForAsync(() => {
    mockMealGenerationService = jasmine.createSpyObj('MealGenerationService', [
      'getDailyMeals',
    ]);

    mockDaysMeals = {
      breakfast: {
        name: 'test',
        description: 'test',
        image: 'test',
        ingredients: 'test',
        instructions: 'test',
        cookingTime: 'test',
        type: 'breakfast',
      },
      lunch: {
        name: 'test',
        description: 'test',
        image: 'test',
        ingredients: 'test',
        instructions: 'test',
        cookingTime: 'test',
        type: 'breakfast',
      },
      dinner: {
        name: 'test',
        description: 'test',
        image: 'test',
        ingredients: 'test',
        instructions: 'test',
        cookingTime: 'test',
        type: 'breakfast',
      },
      mealDay: 'tuesday',
      mealDate: new Date(),
    };

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), DailyMealsComponent, HttpClientModule],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DailyMealsComponent);
    component = fixture.componentInstance;
    component.dayData = mockDaysMeals;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
