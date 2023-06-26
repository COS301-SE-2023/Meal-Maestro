import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { DailyMealsComponent } from './daily-meals.component';

describe('DailyMealsComponent', () => {
  let component: DailyMealsComponent;
  let fixture: ComponentFixture<DailyMealsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), DailyMealsComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DailyMealsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
