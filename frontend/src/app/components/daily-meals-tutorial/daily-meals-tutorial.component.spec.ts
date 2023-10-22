import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { DailyMealsTutorialComponent } from './daily-meals-tutorial.component';
import { DaysMealsI } from '../../models/interfaces';

describe('DailyMealsTutorialComponent', () => {
  let component: DailyMealsTutorialComponent;
  let fixture: ComponentFixture<DailyMealsTutorialComponent>;
  let mockDaysMeals: DaysMealsI;

  beforeEach(waitForAsync(() => {
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
      imports: [IonicModule.forRoot(), DailyMealsTutorialComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DailyMealsTutorialComponent);
    component = fixture.componentInstance;
    component.dayData = mockDaysMeals;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
