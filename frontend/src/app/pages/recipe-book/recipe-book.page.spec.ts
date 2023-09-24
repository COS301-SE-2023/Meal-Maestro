import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeBookPage } from './recipe-book.page';
import { AuthenticationService, RecipeBookApiService } from '../../services/services';
import { HttpClientModule } from '@angular/common/http';

describe('RecipeBookPage', () => {
  let component: RecipeBookPage;
  let fixture: ComponentFixture<RecipeBookPage>;
  let mockRecipeBookApiService: jasmine.SpyObj<RecipeBookApiService>;
  let authServiceSpy: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule, RecipeBookPage],
      providers: [
        { provide: RecipeBookApiService, useValue: mockRecipeBookApiService },
        { provide: AuthenticationService, useValue: authServiceSpy },
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
