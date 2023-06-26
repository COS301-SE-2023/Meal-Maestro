import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowsePage } from './browse.page';
//import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { IonicModule } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';

describe('BrowsePage', () => {
  let component: BrowsePage;
  let fixture: ComponentFixture<BrowsePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowsePage, IonicModule, RecipeItemComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BrowsePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
