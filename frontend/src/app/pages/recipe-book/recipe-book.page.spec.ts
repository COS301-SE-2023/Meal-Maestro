import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeBookPage } from './recipe-book.page';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';

describe('RecipeBookPage', () => {
  let component: RecipeBookPage;
  let fixture: ComponentFixture<RecipeBookPage>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [RecipeBookPage],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(RecipeBookPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
