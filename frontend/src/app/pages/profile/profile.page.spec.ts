import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ProfilePage } from './profile.page';

describe('ProfilePage', () => {
  let component: ProfilePage;
  let fixture: ComponentFixture<ProfilePage>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [IonicModule.forRoot(), FormsModule, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to account-profile page', () => {
    const routerSpy = spyOn(component['router'], 'navigate');
    component.navToProfile();
    expect(routerSpy).toHaveBeenCalledWith(['acc-profile']);
  });

  it('should handle price range change for "custom" option', () => {
    component.selectedPriceRange = 'custom';
    component.handlePriceRangeChange();
    // Add your assertions here
  });

  it('should set open preferences modal', () => {
    component.setOpenPreferences(true);
    expect(component.isPreferencesModalOpen).toBe(true);
  });

  it('should set open preferences modal and save changes', () => {
    component.userpreferences.foodpreference_set = false; // Set the flag to false
    component.userpreferences.food_preferences = ['Vegetarian']; // Set the initial preferences
  
    // Trigger the method with isOpen set to true
    component.setOpenPreferencesSave(true);
  
    expect(component.userpreferences.food_preferences.length).toEqual(0); // Array should be cleared
    expect(component.displayPreferences).toEqual(''); // displayPreferences should be an empty string
    expect(component.isPreferencesModalOpen).toEqual(true); // isPreferencesModalOpen should be true
  });
  
  
  // Add more test cases for other methods and functionalities
});
