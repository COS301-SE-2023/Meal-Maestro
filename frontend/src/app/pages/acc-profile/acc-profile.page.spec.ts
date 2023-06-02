import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccProfilePage } from './acc-profile.page';
import { IonicModule } from '@ionic/angular';

describe('AccProfilePage', () => {
  let component: AccProfilePage;
  let fixture: ComponentFixture<AccProfilePage>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AccProfilePage, IonicModule],
    }).compileComponents();
    fixture = TestBed.createComponent(AccProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
