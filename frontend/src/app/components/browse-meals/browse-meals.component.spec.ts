import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { BrowseMealsComponent } from './browse-meals.component';
import { MealI } from '../../models/interfaces';
import { HttpClientModule } from '@angular/common/http';

describe('BrowseMealsComponent', () => {
  let component: BrowseMealsComponent;
  let fixture: ComponentFixture<BrowseMealsComponent>;
  let mockMeal: MealI;

  beforeEach(waitForAsync(() => {
    mockMeal = {
      name: 'test',
      description: 'test',
      image: 'test',
      ingredients: 'test',
      instructions: 'test',
      cookingTime: 'test',
      type: 'breakfast',
    };

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), BrowseMealsComponent, HttpClientModule],
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
