import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowsePage } from './browse.page';
//import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { IonicModule } from '@ionic/angular';

import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';
import {
  AuthenticationService,
  MealGenerationService,
} from '../../services/services';

describe('BrowsePage', () => {
  let component: BrowsePage;
  let fixture: ComponentFixture<BrowsePage>;
  let mockMealGenerationService: jasmine.SpyObj<MealGenerationService>;
  let mockAuthService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('AuthenticationService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [BrowsePage, IonicModule, RecipeItemComponent],
      providers: [
        { provide: MealGenerationService, useValue: mockMealGenerationService },
        { provide: AuthenticationService, useValue: mockAuthService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BrowsePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
