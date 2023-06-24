import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { FoodListItemComponent } from './food-list-item.component';

describe('FoodListItemComponent', () => {
  let component: FoodListItemComponent;
  let fixture: ComponentFixture<FoodListItemComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), FoodListItemComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FoodListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
