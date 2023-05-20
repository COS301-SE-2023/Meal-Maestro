import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccProfilePage } from './acc-profile.page';

describe('AccProfilePage', () => {
  let component: AccProfilePage;
  let fixture: ComponentFixture<AccProfilePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(AccProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
