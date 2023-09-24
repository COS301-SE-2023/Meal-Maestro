import { Component, OnInit } from '@angular/core';
import {
  IonicModule,
  PickerController,
  ViewWillEnter,
  ToastController,
} from '@ionic/angular';
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
    private errorHandlerService: ErrorHandlerService,
    private toastController: ToastController
  ) {}
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

  displaying_Macroratio: string | undefined;
  shoppingIntervalOtherValue: number | undefined | any = 7;
  shoppingInterval: string | any;
  displayAllergies: string[] | string = '';
  displayPreferences: string[] | string = '';
  selectedPreferences: string | any;
  selectedPriceRange: string | any;

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

  isPreferencesModalOpen: boolean = false;
  isCalorieModalOpen: boolean = false;
  isBudgetModalOpen: boolean = false;
  isMacroModalOpen: boolean = false;
  isAllergiesModalOpen: boolean = false;
  isCookingModalOpen: boolean = false;
  isBMIModalOpen: boolean = false;
  isShoppingModalOpen: boolean = false;

  //toggle logic
  shoppingintervalToggle: boolean = false;
  preferenceToggle: boolean = false;
  calorieToggle: boolean = false;
  budgetToggle: boolean = false;
  macroToggle: boolean = false;
  allergiesToggle: boolean = false;
  cookingToggle: boolean = false;
  BMIToggle: boolean = false;


  //reset logic for cancel button
  initialshoppinginterval: string | any;
  initialpreference: string | any;
  initialpreferenceVegetarian: string | any;
  initialpreferenceVegan: string | any;
  initialpreferenceGlutenIntolerant: string | any;
  initialpreferenceLactoseIntolerant: string | any;
  initialcalorie: number | any;
  initialbudget: string | any;
  initialprotein: number | any;
  initialcarbs: number | any;
  initialfat: number | any;
  initialallergies: string | any;
  initialallergiesSeafood: string | any;
  initialallergiesNuts: string | any;
  initialallergiesEggs: string | any;
  initialallergiesSoy: string | any;

  initialcooking: string | any;
  initialBMI: number | any;
  initialshoppingintervalToggle: boolean | any;
  initialpreferenceToggle: boolean | any;
  initialcalorieToggle: boolean | any;
  initialbudgetToggle: boolean | any;
  initialmacroToggle: boolean | any;
  initialallergiesToggle: boolean | any;
  initialcookingToggle: boolean | any;
  initialBMIToggle: boolean | any;
  
  ngOnInit() {}

  ionViewWillEnter(): void {
    if (!this.loginService.isSettingsRefreshed()) {
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
      this.loginService.setSettingsRefreshed(true);
    }
  }

  private async loadUserSettings() {
    this.settingsApiService.getSettings().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            console.log('loaduser');
            console.log(response.body);
            this.settings.goal = response.body.goal;
            if (response.body.shoppingInterval != '') {
              this.settings.shoppingIntervalSet = true;
            }

            if (
              response.body.shoppingInterval === 'weekly' ||
              response.body.shoppingInterval === 'biweekly' ||
              response.body.shoppingInterval === 'monthly'
            ) {
              this.settings.shoppingInterval = response.body.shoppingInterval;
              this.shoppingInterval = response.body.shoppingInterval;
            } else if (response.body.shoppingInterval.includes('days')) {
              this.settings.shoppingInterval = 'other';
              this.shoppingIntervalOtherValue = response.body.shoppingInterval;
            } else {
              this.settings.shoppingIntervalSet = false;
              this.settings.shoppingInterval = '';
              this.shoppingInterval = '';
            }

            this.settings.foodPreferences = response.body.foodPreferences;
            if (response.body.calorieAmount == 0) {
              this.settings.calorieAmount = '';
            } else {
              this.settings.calorieAmount = response.body.calorieAmount;
            }

            console.log('budget');
            console.log(response.body.budgetRange);
            if (response.body.budgetRange.includes('R')) {
              this.settings.budgetRange = response.body.budgetRange;
              this.selectedPriceRange = 'custom';
            } else {
              this.settings.budgetRange = response.body.budgetRange;
              this.selectedPriceRange = response.body.budgetRange;
            }
            this.settings.allergies = response.body.allergies;
            this.settings.cookingTime = response.body.cookingTime;
            this.settings.userHeight = response.body.userHeight;
            this.settings.userWeight = response.body.userWeight;
            if (response.body.userBMI == 0) {
              this.settings.userBMI = '';
            } else this.settings.userBMI = response.body.userBMI;
            this.settings.bmiset = response.body.bmiset;
            this.settings.cookingTimeSet = response.body.cookingTimeSet;
            this.settings.allergiesSet = response.body.allergiesSet;
            if (
              response.body.protein > 0 &&
              response.body.carbs > 0 &&
              response.body.fat > 0 &&
              response.body.macroSet === true
            ) {
              this.settings.macroSet = true;
            } else if (
              response.body.protein === 0 ||
              response.body.carbs === 0 ||
              response.body.fat === 0 ||
              response.body.macroSet === false
            ) {
              this.settings.macroSet = false;
            }
            this.settings.budgetSet = response.body.budgetSet;
            this.settings.calorieSet = response.body.calorieSet;
            this.settings.shoppingIntervalSet =
              response.body.shoppingIntervalSet;
            this.settings.fat = response.body.fat;
            this.settings.carbs = response.body.carbs;
            this.settings.protein = response.body.protein;
            this.displayPreferences = this.settings.foodPreferences;
            this.displayAllergies = this.settings.allergies;

            this.shoppingintervalToggle = this.settings.shoppingIntervalSet;
            this.calorieToggle = this.settings.calorieSet;
            this.budgetToggle = this.settings.budgetSet;

            this.allergiesToggle = this.settings.allergiesSet;
            this.cookingToggle = this.settings.cookingTimeSet;
            this.BMIToggle = this.settings.bmiset;
            this.shoppingInterval = this.settings.shoppingInterval;

            this.displaying_Macroratio = this.getDisplayMacroratio();
            this.updateDisplayData();

            this.setInitialAllergies();
            this.setInitialBMI();
            this.setInitialBudget();
            this.setInitialCalorie();
            this.setInitialCooking();
            this.setIntialPreference();
            this.setInitialMacro();
            this.setInitialShopping();
            this.setInitialBMI();
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
    console.log('update');
    //if check to ensure only 1 "days" is in the string
    if (
      this.settings.shoppingInterval.includes('days') &&
      this.settings.shoppingInterval.split('days').length - 1 > 1
    ) {
      this.settings.shoppingInterval = this.settings.shoppingInterval
        .replace('days', '')
        .trim();
    }

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
    this.resetPreference();
  }

  setOpenPreferencesSave(isOpen: boolean) {
    console.log('Saving Preferences');
    if (this.settings.foodPreferenceSet === true) {
      this.getSelectedPreferences(); // This will update this.settings.foodPreferences
      if (
        !this.preferences.vegetarian &&
        !this.preferences.vegan &&
        !this.preferences.glutenIntolerant &&
        !this.preferences.lactoseIntolerant
      ) {
        this.presentToast(
          'Please select at least one food preference. If you have no food preferences, please uncheck the food preferences toggle.'
        );
        return;
      }
      if (!isOpen) {
        this.updateDisplayData(); // Update the display data when the modal is closed
      }
      this.isPreferencesModalOpen = isOpen;
    } else {
      this.settings.foodPreferences = [];
      this.displayPreferences = '';
      this.isPreferencesModalOpen = isOpen;
    }
    this.setIntialPreference();
    this.updateSettingsOnServer();
  }

  preference_Toggle() {
    this.settings.foodPreferenceSet = !this.settings.foodPreferenceSet;
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
    this.resetCalorie();
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
      this.settings.calorieAmount = '';
      this.isCalorieModalOpen = isOpen;
    }
    this.setInitialCalorie();
    this.updateSettingsOnServer();
  }

  calorieAmount_Toggle() {
    this.settings.calorieSet = !this.settings.calorieSet;
  }

  showSelectedCalorieAmount(event: any) {
    this.settings.calorieAmount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
    this.resetBudget();
  }

  setOpenBudgetSave(isOpen: boolean) {
    console.log('setOpenBudgetSave called with:', isOpen); // Debug 1

    if (this.settings.budgetSet === true) {
      console.log('Budget is set.'); // Debug 2

      if (this.selectedPriceRange === 'custom') {
        console.log('Custom range selected.'); // Debug 3

        if (
          this.settings.budgetRange !== null &&
          this.settings.budgetRange !== undefined
        ) {
          console.log('Budget range is neither null nor undefined.'); // Debug 4

          const budgetString = this.settings.budgetRange.toString();
          const rCount = (budgetString.match(/R/g) || []).length;

          const isValid = /^[R]?[0-9\s]*$/.test(budgetString);

          if (!isValid) {
            this.settings.budgetRange = budgetString.replace(/[^0-9R\s]/g, '');
          }

          if (rCount > 1) {
            this.settings.budgetRange = budgetString.replace(/R/g, '').trim();
            this.settings.budgetRange = 'R ' + this.settings.budgetRange;
          } else if (rCount === 0) {
            this.settings.budgetRange = 'R ' + budgetString;
          }
        }
      } else {
        this.settings.budgetRange = this.selectedPriceRange;
      }

      this.isBudgetModalOpen = false;
      console.log('Attempting to close the modal.'); // Debug 5
    } else {
      this.settings.budgetRange = '';
      this.isBudgetModalOpen = false;
    }

    this.setInitialBudget();
    this.updateSettingsOnServer();
    console.log('Function completed.'); // Debug 6
  }

  validateBudgetInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    input.value = value.replace(/[^0-9.]/g, '');
  }

  budgetRange_Toggle() {
    this.settings.budgetSet = !this.settings.budgetSet;
    if (!this.settings.budgetSet && this.selectedPriceRange === 'custom') {
      this.settings.budgetRange = '';
    }
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
          },
        },
      ],
    });

    await picker.present();
  }
  setOpenMacro(isOpen: boolean) {
    this.isMacroModalOpen = isOpen;
    this.resetMacro();
  }
  setOpenMacroSave(isOpen: boolean) {
    if (this.settings.macroSet === true) {
      if (!isOpen) {
        this.displaying_Macroratio = this.getDisplayMacroratio();
      }

      this.isMacroModalOpen = isOpen;
    } else if (this.settings.macroSet === false) {
      this.settings.protein = 0;
      this.settings.carbs = 0;
      this.settings.fat = 0;
      this.displaying_Macroratio = '';
      this.isMacroModalOpen = isOpen;
    }
    this.setInitialMacro();
    this.updateSettingsOnServer();
  }
  macro_Toggle() {
    this.settings.macroSet = !this.settings.macroSet;
  }
  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
    this.resetAllergies();
  }
  setOpenAllergiesSave(isOpen: boolean) {
    if (this.settings.allergiesSet === true) {
      if (
        !this.allergens.seafood &&
        !this.allergens.nuts &&
        !this.allergens.eggs &&
        !this.allergens.soy
      ) {
        this.presentToast(
          'Please select at least one allergen. if you have no allergies, please uncheck the allergies toggle.'
        );
        return;
      }
      if (!isOpen) {
        this.displayAllergies = this.getSelectedAllergens(); // Update the display data when the modal is closed
      }
      this.isAllergiesModalOpen = isOpen;
    } else {
      this.settings.allergies = [];
      this.displayAllergies = '';
      this.isAllergiesModalOpen = isOpen;
    }
    this.setInitialAllergies();
    this.updateSettingsOnServer();
  }

  allergies_Toggle() {
    this.settings.allergiesSet = !this.settings.allergiesSet;
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
    this.resetCooking();
    this.isCookingModalOpen = isOpen;
  }
  setOpenCookingSave(isOpen: boolean) {
    if (this.settings.cookingTimeSet === true) {
      this.isCookingModalOpen = isOpen;
    } else if (this.settings.cookingTimeSet === false) {
      this.settings.cookingTime = '';
      this.isCookingModalOpen = isOpen;
    }
    this.setInitialCooking();
    this.updateSettingsOnServer();
  }
  cookingtime_Toggle() {
    this.settings.cookingTimeSet = !this.settings.cookingTimeSet;
  }
  setOpenBMI(isOpen: boolean) {
    this.resetBMI();
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {
    if (
      this.settings.bmiset === true &&
      this.settings.userHeight > 0 &&
      this.settings.userWeight > 0
    ) {
      if (this.settings.userHeight > 0 && this.settings.userWeight > 0) {
        this.calculateBMI();
        this.updateDisplayData();
        this.updateSettingsOnServer();
        this.isBMIModalOpen = isOpen;
      }
    }
    if (this.settings.bmiset === false) {
      this.settings.userHeight = 0;
      this.settings.userWeight = 0;
      this.settings.userBMI = '';
      this.isBMIModalOpen = isOpen;
    }
    this.setInitialBMI();
    this.updateSettingsOnServer();
  }

  BMI_Toggle() {
    this.settings.bmiset = !this.settings.bmiset;
  }

  setOpenShopping(isOpen: boolean) {
    if (isOpen === false) {
      console.log('resetClose');
      console.log(this.settings.shoppingInterval);
      this.resetShopping();
      console.log(this.settings.shoppingInterval);
      this.isShoppingModalOpen = isOpen;
    } else if (isOpen === true) {
      console.log('resetOpen');
      if (this.settings.shoppingInterval.includes('days')) {
        this.shoppingInterval = 'other';
        this.shoppingIntervalOtherValue = this.settings.shoppingInterval
          .replace('days', '')
          .trim();
      }
      console.log(this.settings.shoppingInterval);

      this.isShoppingModalOpen = isOpen;
    }
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.settings.shoppingIntervalSet === true) {
      if (this.shoppingInterval === 'other') {
        this.settings.shoppingInterval =
          this.shoppingIntervalOtherValue.toString() + ' days';
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
    } else if (
      this.settings.shoppingIntervalSet === false &&
      this.shoppingInterval === 'other'
    ) {
      this.settings.shoppingInterval = '';
      this.isShoppingModalOpen = isOpen;
    }
    this.setInitialShopping();
    this.updateSettingsOnServer();
  }

  shoppingInterval_Toggle() {
    this.settings.shoppingIntervalSet = !this.settings.shoppingIntervalSet;
    if (this.settings.shoppingIntervalSet === false) {
      this.shoppingInterval = '';
    }
  }

  // Function to determine what to display for Shopping Interval
  getDisplayShoppingInterval() {
    if (this.shoppingInterval === 'other') {
      if (this.shoppingIntervalOtherValue.toString().includes('days')) {
        return this.shoppingIntervalOtherValue;
      } else {
        return this.shoppingIntervalOtherValue + ' days';
      }
    } else {
      return this.settings.shoppingInterval;
    }
  }

  getDisplayOtherShoppingInterval() {
    if (this.shoppingIntervalOtherValue.toString().includes('days')) {
      return this.shoppingIntervalOtherValue
        .toString()
        .replace('days', '')
        .trim();
    } else {
      return this.shoppingIntervalOtherValue;
    }
  }

  // Function to update display data
  updateDisplayData() {
    if (this.settings.shoppingIntervalSet === true) {
      this.shoppingintervalToggle = true;
      this.shoppingInterval = this.settings.shoppingInterval;
      this.settings.shoppingIntervalSet = true;
    }

    if (
      this.settings.foodPreferences != null &&
      this.settings.foodPreferences.length != 0
    ) {
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

    console.log('budgetupdatedisplay');
    console.log(this.settings.budgetRange);

    // Convert budgetRange to a string to avoid type errors
    const budgetString = this.settings.budgetRange
      ? this.settings.budgetRange.toString()
      : '';

    // Check for the presence of 'R' and whether it's a custom range
    if (
      budgetString.includes('R') &&
      ['low', 'moderate', 'high'].indexOf(
        budgetString.toLowerCase().replace('r ', '')
      ) === -1
    ) {
      this.budgetToggle = true;
      this.selectedPriceRange = 'custom';
      this.settings.budgetSet = true;
      console.log('budget- custom');
    } else {
      this.budgetToggle = true;
      this.selectedPriceRange = budgetString.replace('R ', '');
      this.settings.budgetSet = true;
      console.log('budget- not custom');
    }

    if (
      this.settings.allergies != null &&
      this.settings.allergies.length != 0
    ) {
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
    this.settings.allergiesSet = true;

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

  setInitialShopping() {
    this.initialshoppinginterval = this.settings.shoppingInterval;
    this.initialshoppingintervalToggle = this.settings.shoppingIntervalSet;
  }

  setIntialPreference() {
    console.log('Setting Initial Preferences');
    this.initialpreference = this.settings.foodPreferences;
    this.initialpreferenceToggle = this.settings.foodPreferenceSet;
    this.initialpreferenceVegetarian = this.preferences.vegetarian;
    this.initialpreferenceVegan = this.preferences.vegan;
    this.initialpreferenceGlutenIntolerant = this.preferences.glutenIntolerant;
    this.initialpreferenceLactoseIntolerant =
      this.preferences.lactoseIntolerant;
  }

  setInitialCalorie() {
    this.initialcalorie = this.settings.calorieAmount;
    this.initialcalorieToggle = this.settings.calorieSet;
  }

  setInitialBudget() {
    this.initialbudget = this.settings.budgetRange;
    this.initialbudgetToggle = this.settings.budgetSet;
  }

  setInitialMacro() {
    this.initialprotein = this.settings.protein;
    this.initialcarbs = this.settings.carbs;
    this.initialfat = this.settings.fat;
    this.initialmacroToggle = this.settings.macroSet;
  }

  setInitialAllergies() {
    this.initialallergiesSeafood = this.allergens.seafood;
    this.initialallergiesNuts = this.allergens.nuts;
    this.initialallergiesEggs = this.allergens.eggs;
    this.initialallergiesSoy = this.allergens.soy;
    this.initialallergies = this.settings.allergies;
    this.initialallergiesToggle = this.settings.allergiesSet;
  }

  setInitialCooking() {
    this.initialcooking = this.settings.cookingTime;
    this.initialcookingToggle = this.settings.cookingTimeSet;
  }

  setInitialBMI() {
    this.initialBMI = this.settings.userBMI;
    this.initialBMIToggle = this.settings.bmiset;
  }

  resetShopping() {
    if (this.settings.shoppingInterval.includes('days')) {
      const temp = this.settings.shoppingInterval;
      this.settings.shoppingInterval = 'other';
      this.shoppingIntervalOtherValue = temp;
    }

    this.settings.shoppingInterval = this.initialshoppinginterval;
    this.shoppingintervalToggle = this.initialshoppingintervalToggle;
    this.settings.shoppingIntervalSet = this.initialshoppingintervalToggle;
    this.shoppingInterval = this.initialshoppinginterval;
  }

  resetPreference() {
    console.log('Resetting Preferences');
    // Reset both settings and preferences objects
    this.preferences.vegetarian = this.initialpreferenceVegetarian;
    this.preferences.vegan = this.initialpreferenceVegan;
    this.preferences.glutenIntolerant = this.initialpreferenceGlutenIntolerant;
    this.preferences.lactoseIntolerant =
      this.initialpreferenceLactoseIntolerant;

    this.settings.foodPreferences = this.initialpreference;
    this.settings.foodPreferenceSet = this.initialpreferenceToggle;
    this.preferenceToggle = this.initialpreferenceToggle;
    this.displayPreferences = this.initialpreference;
  }

  resetCalorie() {
    this.settings.calorieAmount = this.initialcalorie;
    this.calorieToggle = this.initialcalorieToggle;
    this.settings.calorieSet = this.initialcalorieToggle;
  }

  resetBudget() {
    console.log('resetbudget');
    if (this.initialbudget.includes('R') && this.initialbudgetToggle === true) {
      this.settings.budgetRange = this.initialbudget;
      this.selectedPriceRange = 'custom';
      this.settings.budgetSet = this.initialbudgetToggle;
      this.budgetToggle = this.initialbudgetToggle;
    } else if (
      !this.initialbudget.includes('R') &&
      this.initialbudgetToggle === true
    ) {
      this.settings.budgetRange = this.initialbudget;
      this.selectedPriceRange = this.initialbudget;
      this.settings.budgetSet = this.initialbudgetToggle;
      this.budgetToggle = this.initialbudgetToggle;
    }
  }

  resetMacro() {
    this.settings.protein = this.initialprotein;
    this.settings.carbs = this.initialcarbs;
    this.settings.fat = this.initialfat;
    this.settings.macroSet = this.initialmacroToggle;
    this.macroToggle = this.initialmacroToggle;
    this.displaying_Macroratio = this.getDisplayMacroratio();
  }

  resetAllergies() {
    this.allergens.seafood = this.initialallergiesSeafood;
    this.allergens.nuts = this.initialallergiesNuts;
    this.allergens.eggs = this.initialallergiesEggs;
    this.allergens.soy = this.initialallergiesSoy;
    this.settings.allergies = this.initialallergies;
    this.settings.allergiesSet = this.initialallergiesToggle;
    this.allergiesToggle = this.initialallergiesToggle;
    this.displayAllergies = this.initialallergies;
  }

  resetCooking() {
    this.settings.cookingTime = this.initialcooking;
    this.settings.cookingTimeSet = this.initialcookingToggle;
    this.cookingToggle = this.initialcookingToggle;
  }

  resetBMI() {
    this.settings.userBMI = this.initialBMI;
    this.settings.bmiset = this.initialBMIToggle;
    this.BMIToggle = this.initialBMIToggle;
  }

  disabledConfirmShopping(): boolean {
    if (this.settings.shoppingIntervalSet) {
      return !this.shoppingInterval;
    }
    return false;
  }

  disabledConfirmPreference(): boolean {
    if (this.settings.foodPreferenceSet) {
      return !this.displayPreferences;
    }
    return false;
  }

  disabledConfirmBudget(): boolean {
    if (this.settings.budgetSet) {
      return !this.selectedPriceRange;
    }
    return false;
  }

  disabledCalorieCookingTime(): boolean {
    if (this.settings.cookingTimeSet) {
      return !this.settings.cookingTime;
    }
    return false;
  }

  async presentToast(message: string) {
    const toast = await this.toastController.create({
      message: message,
      duration: 2000,
    });
    toast.present();
  }
}
