import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowsePage } from './browse.page';
import { IonicModule } from '@ionic/angular';

describe('BrowsePage', () => {
  let component: BrowsePage;
  let fixture: ComponentFixture<BrowsePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowsePage, IonicModule],
    }).compileComponents();

    fixture = TestBed.createComponent(BrowsePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
