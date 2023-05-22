import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShoppingPage } from './shopping.page';
import { IonicModule } from '@ionic/angular';

describe('ShoppingPage', () => {
  let component: ShoppingPage;
  let fixture: ComponentFixture<ShoppingPage>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ShoppingPage, IonicModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ShoppingPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
