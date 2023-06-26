import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { BrowseMealsComponent } from './browse-meals.component';

describe('BrowseMealsComponent', () => {
  let component: BrowseMealsComponent;
  let fixture: ComponentFixture<BrowseMealsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), BrowseMealsComponent ]
    }).compileComponents();

    fixture = TestBed.createComponent(BrowseMealsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
