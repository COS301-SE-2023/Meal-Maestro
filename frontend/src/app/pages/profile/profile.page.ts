import { Component, OnInit } from '@angular/core';
import { IonicModule, PickerController, ViewWillEnter } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { SettingsI } from '../../models/settings.model';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { UserI } from '../../models/user.model';
import {
  AuthenticationService,
  ErrorHandlerService,
  LoginService,
  SettingsApiService,
} from '../../services/services';

@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule],
})
export class ProfilePage implements OnInit, ViewWillEnter {
  constructor(
    private router: Router,
    private pickerController: PickerController,
    private settingsApiService: SettingsApiService,
    private auth: AuthenticationService,
    private loginService: LoginService,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.selectedPriceRange = '';
  }

  // User data
  user: UserI = {
    username: '',
    email: '',
    password: '',
  };

  settings: SettingsI = {
    goal: '',
    shoppingInterval: '',
    foodPreferences: [],
    calorieAmount: 0,
    budgetRange: '',
    protein: 0,
    carbs: 0,
    fat: 0,
    allergies: [],
    cookingTime: '',
    userHeight: 0,
    userWeight: 0,
    userBMI: 0,

    bmiset: false,
    cookingTimeSet: false,
    allergiesSet: false,
    macroSet: false,
    budgetSet: false,
    calorieSet: false,
    foodPreferenceSet: false,
    shoppingIntervalSet: false,
  };

  // Variables for displaying
  displaying_Macroratio: string | undefined;
  shoppingIntervalOtherValue: number | undefined | any;
  shoppingInterval: string | any;
  displayAllergies: string[] | string = '';
  displayPreferences: string[] | string = '';
  selectedPreferences: string | any;
  selectedPriceRange: string;

  // Check if possible to change
  preferences = {
    vegetarian: false,
    vegan: false,
    glutenIntolerant: false,
    lactoseIntolerant: false,
  };

  allergens = {
    nuts: false,
    seafood: false,
    soy: false,
    eggs: false,
  };

  // Modal controllers
  isPreferencesModalOpen: boolean = false;
  isCalorieModalOpen: boolean = false;
  isBudgetModalOpen: boolean = false;
  isMacroModalOpen: boolean = false;
  isAllergiesModalOpen: boolean = false;
  isCookingModalOpen: boolean = false;
  isBMIModalOpen: boolean = false;
  isShoppingModalOpen: boolean = false;

  //
  shoppingintervalToggle: boolean = false;
  preferenceToggle: boolean = false;
  calorieToggle: boolean = false;
  budgetToggle: boolean = false;
  macroToggle: boolean = false;
  allergiesToggle: boolean = false;
  cookingToggle: boolean = false;
  BMIToggle: boolean = false;

  ngOnInit() {
    this.loadUserSettings();
    this.auth.getUser().subscribe({
      next: (response) => {
        if (response.status == 200) {
          if (response.body && response.body.name) {
            this.user.username = response.body.name;
            this.user.email = response.body.email;
            this.user.password = response.body.password;
          }
        }
      },
      error: (error) => {
        if (error.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            error
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Unexpected error while loading user data',
            error
          );
        }
      },
    });
  }

  ionViewWillEnter(): void {
    if (this.loginService.isSettingsRefreshed()) {
      this.loadUserSettings();
      this.auth.getUser().subscribe({
        next: (response) => {
          if (response.status == 200) {
            if (response.body && response.body.name) {
              this.user.username = response.body.name;
              this.user.email = response.body.email;
              this.user.password = response.body.password;
            }
          }
        },
        error: (error) => {
          if (error.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              error
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Unexpected error while loading user data',
              error
            );
          }
        },
      });
      this.loginService.setSettingsRefreshed(false);
    }
  }

  private async loadUserSettings() {
    this.settingsApiService.getSettings().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.settings.goal = response.body.goal;
            this.settings.shoppingInterval = response.body.shoppingInterval;
            this.settings.foodPreferences = response.body.foodPreferences;

            if (response.body.calorieAmount == null) {
              this.settings.calorieAmount = 0;
            } else this.settings.calorieAmount = response.body.calorieAmount;

            this.settings.budgetRange = response.body.budgetRange;
            this.settings.allergies = response.body.allergies;
            this.settings.cookingTime = response.body.cookingTime;
            this.settings.userHeight = response.body.userHeight;
            this.settings.userWeight = response.body.userWeight;
            this.settings.userBMI = response.body.userBMI;
            this.settings.bmiset = response.body.bmiset;
            this.settings.cookingTimeSet = response.body.cookingTimeSet;
            this.settings.allergiesSet = response.body.allergiesSet;
            this.settings.macroSet = response.body.macroSet;
            this.settings.budgetSet = response.body.budgetSet;
            this.settings.calorieSet = response.body.calorieSet;
            this.settings.foodPreferenceSet = response.body.foodPreferenceSet;
            this.settings.shoppingIntervalSet =
              response.body.shoppingIntervalSet;
            this.settings.fat = response.body.fat;
            this.settings.carbs = response.body.carbs;
            this.settings.protein = response.body.protein;

            this.displayPreferences = this.settings.foodPreferences;
            this.displayAllergies = this.settings.allergies;
            this.displaying_Macroratio = this.getDisplayMacroratio();
            this.updateDisplayData();
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Unexpected error while loading user settings',
            err
          );
        }
      },
    });
  }

  setGoal() {
    this.updateSettingsOnServer(); // Update the settings on the server when the goal is set
  }

  private updateSettingsOnServer() {
    console.log(this.settings);
    this.settingsApiService.updateSettings(this.settings).subscribe({
      next: (response) => {
        if (response.status === 200) {
          // Successfully updated settings on the server
          console.log('Settings updated successfully');
        }
      },
      error: (error) => {
        // Handle error while updating settings
        if (error.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            error
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Unexpected error while updating settings',
            error
          );
        }
      },
    });
  }

  // Function to navigate to account-profile page
  navToProfile() {
    this.router.navigate(['acc-profile']);
  }

  handlePriceRangeChange() {
    if (this.selectedPriceRange === 'custom') {
      // Perform any custom logic when the "Custom Amount" option is selected
    }
  }

  setOpenPreferences(isOpen: boolean) {
    this.isPreferencesModalOpen = isOpen;
  }

  setOpenPreferencesSave(isOpen: boolean) {
    if (this.settings.foodPreferenceSet === true) {
      if (
        this.preferences.vegetarian ||
        this.preferences.vegan ||
        this.preferences.glutenIntolerant ||
        this.preferences.lactoseIntolerant
      ) {
        if (!isOpen) {
          this.updateDisplayData(); // Update the display data when the modal is closed
        }
        this.isPreferencesModalOpen = isOpen;
      }
    } else if (this.settings.foodPreferenceSet === false) {
      this.settings.foodPreferences = [];
      this.displayPreferences = '';
      this.isPreferencesModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  preference_Toggle() {
    this.settings.foodPreferenceSet = !this.settings.foodPreferenceSet;
    this.updateSettingsOnServer();
  }

  getSelectedPreferences(): string {
    const selectedPreferences = [];
    if (this.settings.foodPreferences == null) {
      this.settings.foodPreferences = [];
      return '';
    } else {
      this.settings.foodPreferences = [];
      if (
        this.preferences.vegetarian &&
        !this.settings.foodPreferences.includes('Vegetarian')
      ) {
        console.log('here');
        selectedPreferences.push('Vegetarian');
        this.settings.foodPreferences.push('Vegetarian');
      }
      if (
        this.preferences.vegan &&
        !this.settings.foodPreferences.includes('Vegan')
      ) {
        selectedPreferences.push('Vegan');
        this.settings.foodPreferences.push('Vegan');
      }
      if (
        this.preferences.glutenIntolerant &&
        !this.settings.foodPreferences.includes('Gluten-intolerant')
      ) {
        selectedPreferences.push('Gluten-intolerant');
        this.settings.foodPreferences.push('Gluten-intolerant');
      }
      if (
        this.preferences.lactoseIntolerant &&
        !this.settings.foodPreferences.includes('Lactose-intolerant')
      ) {
        selectedPreferences.push('Lactose-intolerant');
        this.settings.foodPreferences.push('Lactose-intolerant');
      }

      if (selectedPreferences.length === 1) {
        return selectedPreferences[0];
      } else if (selectedPreferences.length > 1) {
        return 'Multiple';
      } else {
        return '';
      }
    }
  }

  setOpenCalorie(isOpen: boolean) {
    this.isCalorieModalOpen = isOpen;
  }

  setOpenCalorieSave(isOpen: boolean) {
    if (this.settings.calorieSet === true) {
      if (this.settings.calorieAmount) {
        if (!isOpen) {
          this.updateDisplayData(); // Update the display data when the modal is closed
        }
        this.isCalorieModalOpen = isOpen;
      }
    } else if (this.settings.calorieSet === false) {
      this.settings.calorieAmount = 0;
      this.isCalorieModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  calorieAmount_Toggle() {
    this.settings.calorieSet = !this.settings.calorieSet;
    this.updateSettingsOnServer();
  }

  showSelectedCalorieAmount(event: any) {
    this.settings.calorieAmount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }

  setOpenBudgetSave(isOpen: boolean) {
    if (this.settings.budgetSet === true) {
      this.settings.budgetRange = this.selectedPriceRange;
      this.isBudgetModalOpen = isOpen;
    } else if (this.settings.budgetSet === false) {
      this.settings.budgetRange = '';
      this.isBudgetModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  budgetRange_Toggle() {
    this.settings.budgetSet = !this.settings.budgetSet;
    this.updateSettingsOnServer();
  }

  async openPicker() {
    const picker = await this.pickerController.create({
      columns: [
        {
          name: 'protein',
          options: [
            { text: '1', value: 1 },
            { text: '2', value: 2 },
            { text: '3', value: 3 },
            { text: '4', value: 4 },
            { text: '5', value: 5 },
          ],
          selectedIndex: this.settings.protein, // Set the default selected index
        },
        {
          name: 'carbs',
          options: [
            { text: '1', value: 1 },
            { text: '2', value: 2 },
            { text: '3', value: 3 },
            { text: '4', value: 4 },
            { text: '5', value: 5 },
          ],
          selectedIndex: this.settings.carbs, // Set the default selected index
        },
        {
          name: 'fat',
          options: [
            { text: '1', value: 1 },
            { text: '2', value: 2 },
            { text: '3', value: 3 },
            { text: '4', value: 4 },
            { text: '5', value: 5 },
          ],
          selectedIndex: this.settings.fat, // Set the default selected index
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
        },
        {
          text: 'Confirm',
          handler: (value) => {
            // Update the selected macro values based on the selected indexes
            this.settings.protein = value['protein'].value;
            this.settings.carbs = value['carbs'].value;
            this.settings.fat = value['fat'].value;
            this.updateSettingsOnServer();
          },
        },
      ],
    });

    await picker.present();
  }

  setOpenMacro(isOpen: boolean) {
    this.isMacroModalOpen = isOpen;
  }

  setOpenMacroSave(isOpen: boolean) {
    if (this.settings.macroSet === true) {
      if (!isOpen) {
        this.displaying_Macroratio = this.getDisplayMacroratio(); // Update the display data when the modal is closed
      }
      this.isMacroModalOpen = isOpen;
    } else if (this.settings.macroSet === false) {
      this.settings.protein = 0;
      this.settings.carbs = 0;
      this.settings.fat = 0;
      this.displaying_Macroratio = '';
      this.isMacroModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  macro_Toggle() {
    this.settings.macroSet = !this.settings.macroSet;
    this.updateSettingsOnServer();
  }

  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
  }

  setOpenAllergiesSave(isOpen: boolean) {
    if (this.settings.allergiesSet === true) {
      if (
        this.allergens.seafood ||
        this.allergens.nuts ||
        this.allergens.eggs ||
        this.allergens.soy
      ) {
        if (!isOpen) {
          this.displayAllergies = this.getSelectedAllergens(); // Update the display data when the modal is closed
        }
        this.isAllergiesModalOpen = isOpen;
      }
    } else if (this.settings.allergiesSet === false) {
      this.settings.allergies = [];
      this.displayAllergies = '';
      this.isAllergiesModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  allergies_Toggle() {
    this.settings.allergiesSet = !this.settings.allergiesSet;
    this.updateSettingsOnServer();
  }

  getSelectedAllergens(): string {
    const selectedAllergens = [];
    if (this.settings.allergies == null) {
      this.settings.allergies = [];
      return '';
    } else {
      this.settings.allergies = [];

      if (
        this.allergens.seafood &&
        !this.settings.allergies.includes('Seafood')
      ) {
        selectedAllergens.push('Seafood');
        this.settings.allergies.push('Seafood');
      }
      if (this.allergens.nuts && !this.settings.allergies.includes('Nuts')) {
        selectedAllergens.push('Nuts');
        this.settings.allergies.push('Nuts');
      }
      if (this.allergens.eggs && !this.settings.allergies.includes('Eggs')) {
        selectedAllergens.push('Eggs');
        this.settings.allergies.push('Eggs');
      }
      if (this.allergens.soy && !this.settings.allergies.includes('Soy')) {
        selectedAllergens.push('Soy');
        this.settings.allergies.push('Soy');
      }

      if (selectedAllergens.length === 1) {
        return selectedAllergens[0];
      } else if (selectedAllergens.length > 1) {
        return 'Multiple';
      } else {
        return '';
      }
    }
  }

  setOpenCooking(isOpen: boolean) {
    this.isCookingModalOpen = isOpen;
  }

  setOpenCookingSave(isOpen: boolean) {
    if (this.settings.cookingTimeSet === true) {
      this.isCookingModalOpen = isOpen;
    } else if (this.settings.cookingTimeSet === false) {
      this.settings.cookingTime = '';
      this.isCookingModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  cookingtime_Toggle() {
    this.settings.cookingTimeSet = !this.settings.cookingTimeSet;
    this.updateSettingsOnServer();
  }

  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }

  setOpenBMISave(isOpen: boolean) {
    if (this.settings.userHeight && this.settings.userWeight) {
      this.updateDisplayData(); // Update the display data when the modal is closed
      this.isBMIModalOpen = isOpen;
    }

    if (this.settings.bmiset === false) {
      this.settings.userBMI = 0;
      this.isBMIModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  BMI_Toggle() {
    this.settings.bmiset = !this.settings.bmiset;
    this.updateSettingsOnServer();
  }

  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.settings.shoppingIntervalSet === true) {
      if (this.shoppingInterval === 'other') {
        this.settings.shoppingInterval =
          this.shoppingIntervalOtherValue.toString();
      } else if (
        this.shoppingInterval == 'weekly' ||
        this.shoppingInterval == 'biweekly' ||
        this.shoppingInterval == 'monthly'
      ) {
        this.settings.shoppingInterval = this.shoppingInterval;
      }
      this.isShoppingModalOpen = isOpen;
    } else if (this.settings.shoppingIntervalSet === false) {
      this.settings.shoppingInterval = '';
      this.isShoppingModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  shoppingInterval_Toggle() {
    this.settings.shoppingIntervalSet = !this.settings.shoppingIntervalSet;
    this.updateSettingsOnServer();
  }

  // Function to update display data
  updateDisplayData() {
    if (this.settings.shoppingIntervalSet === true) {
      this.shoppingintervalToggle = true;
      this.shoppingInterval = this.settings.shoppingInterval;
      this.settings.shoppingIntervalSet = true;
    }

    if (this.settings.foodPreferences != null) {
      this.preferenceToggle = true;
      if (this.settings.foodPreferences.includes('Vegetarian')) {
        this.preferences.vegetarian = true;
      }
      if (this.settings.foodPreferences.includes('Vegan')) {
        this.preferences.vegan = true;
      }
      if (this.settings.foodPreferences.includes('Gluten-intolerant')) {
        this.preferences.glutenIntolerant = true;
      }
      if (this.settings.foodPreferences.includes('Lactose-intolerant')) {
        this.preferences.lactoseIntolerant = true;
      }
      this.settings.foodPreferenceSet = true;
    }

    if (this.settings.calorieAmount != 0) {
      this.calorieToggle = true;
      this.settings.calorieSet = true;
    }

    if (this.settings.budgetRange != '') {
      this.budgetToggle = true;
      this.selectedPriceRange = this.settings.budgetRange;
      this.settings.budgetSet = true;
    }

    if (
      this.settings.protein != null &&
      this.settings.carbs != null &&
      this.settings.fat
    ) {
      this.macroToggle = true;

      this.settings.macroSet = true;
    }

    if (this.settings.allergies != null) {
      this.allergiesToggle = true;
      if (this.settings.allergies.includes('Seafood')) {
        this.allergens.seafood = true;
      }
      if (this.settings.allergies.includes('Nuts')) {
        this.allergens.nuts = true;
      }
      if (this.settings.allergies.includes('Eggs')) {
        this.allergens.eggs = true;
      }
      if (this.settings.allergies.includes('Soy')) {
        this.allergens.soy = true;
      }
      this.settings.allergiesSet = true;
    }

    if (this.settings.userBMI != 0) {
      this.BMIToggle = true;
      this.settings.bmiset = true;
    }

    if (this.settings.cookingTime != '') {
      this.cookingToggle = true;
      this.settings.cookingTimeSet = true;
    }

    this.displayPreferences = this.getSelectedPreferences();
    this.displaying_Macroratio = this.getDisplayMacroratio();
    this.displayAllergies = this.getSelectedAllergens();
  }

  // Function to get the displaying macro ratio
  getDisplayMacroratio(): string {
    if (
      this.settings &&
      this.settings.protein &&
      this.settings.carbs &&
      this.settings.fat
    ) {
      return (
        this.settings.protein +
        ' : ' +
        this.settings.carbs +
        ' : ' +
        this.settings.fat
      );
    } else {
      return 'Not available';
    }
  }

  calculateBMI() {
    let heightInMeters = this.settings.userHeight / 100;
    let heightSquared = heightInMeters * heightInMeters;
    let bmi = this.settings.userWeight / heightSquared;
    this.settings.userBMI = parseFloat(bmi.toFixed(2));
  }
}
