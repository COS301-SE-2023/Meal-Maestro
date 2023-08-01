import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { RecipeDetailsComponent } from './recipe-details.component';
import { MealI } from '../../models/meal.model';

describe('RecipeDetailsComponent', () => {
  let component: RecipeDetailsComponent;
  let fixture: ComponentFixture<RecipeDetailsComponent>;
  let mockItem: MealI;
  let mockItems: MealI[];

  beforeEach(waitForAsync(() => {
    mockItem = {
      name: 'test',
      description: 'test',
      ingredients: 'test',
      instructions: 'test',
      image: 'test',
      cookingTime: 'test',
    };

    mockItems = [mockItem];

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), RecipeDetailsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeDetailsComponent);
    component = fixture.componentInstance;
    component.item = mockItem;
    component.items = mockItems;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
