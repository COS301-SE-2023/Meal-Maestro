import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeBookPage } from './recipe-book.page';

describe('RecipeBookPage', () => {
  let component: RecipeBookPage;
  let fixture: ComponentFixture<RecipeBookPage>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [RecipeBookPage],
    }).compileComponents();
    fixture = TestBed.createComponent(RecipeBookPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
