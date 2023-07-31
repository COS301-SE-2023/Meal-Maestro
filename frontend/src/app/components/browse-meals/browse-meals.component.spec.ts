import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { BrowseMealsComponent } from './browse-meals.component';
import { MealBrowseI } from '../../models/mealBrowse.model';

describe('BrowseMealsComponent', () => {
  let component: BrowseMealsComponent;
  let fixture: ComponentFixture<BrowseMealsComponent>;
  let mockMeal: MealBrowseI;

  beforeEach(waitForAsync(() => {
    mockMeal = {
      name: 'test',
      description: 'test',
      url: 'test',
      ingredients: 'test',
      instructions: 'test',
      cookingTime: 'test',
    };

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), BrowseMealsComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(BrowseMealsComponent);
    component = fixture.componentInstance;
    component.mealsData = mockMeal;
    component.searchData = mockMeal;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
