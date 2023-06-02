import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowsePage } from './browse.page';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { IonicModule } from '@ionic/angular';

describe('BrowsePage', () => {
  let component: BrowsePage;
  let fixture: ComponentFixture<BrowsePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowsePage, IonicModule, ExploreContainerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BrowsePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
