import { ComponentFixture, TestBed } from '@angular/core/testing';
//import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { IonicModule } from '@ionic/angular';
import { RecipeComponent } from './recipe.component';

describe('RecipeComponent', () => {
  let component: RecipeComponent;
  let fixture: ComponentFixture<RecipeComponent>;

  beforeEach(async() => {
    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), RecipeComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
