import { Component } from '@angular/core';
import { IonicModule, PickerController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { UserPreferencesI } from '../../models/userpreference.model';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SettingsApiService } from '../../services/settings-api/settings-api.service';

@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule],
})
export class ProfilePage {
  constructor(
    private router: Router,
    private pickerController: PickerController,
    private settingsApiService: SettingsApiService
  ) {
    this.selectedPriceRange = '';
  }

  userpreferences: UserPreferencesI = {
    goal: '',
    shoppingInterval: '',
  foodPreferences: [],
    calorieAmount: 0,
    budgetRange: '',
    macroRatio: { protein: 0, carbs: 0, fat: 0 },
    allergies: [],
    cookingTime: 0,
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
  displayAllergies: string = '';
  displayPreferences: string = '';
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
  }



   private async loadUserSettings() {
    this.settingsApiService.getSettings().subscribe({
    
    
      next: (response) => {
        console.log("response")
         console.log(response.body)
        if (response.status === 200) {
          console.log("response")
          if (response.body) {
           
            // this.userpreferences = response.body;
            this.userpreferences.goal = response.body.goal;
            this.userpreferences.shoppingInterval = response.body.shoppingInterval;
            this.userpreferences.foodPreferences = response.body.foodPreferences;
            if (response.body.calorieAmount == null) {
              this.userpreferences.calorieAmount = 0;
            }
            else
            this.userpreferences.calorieAmount = response.body.calorieAmount;

            this.userpreferences.budgetRange = response.body.budgetRange;
            this.userpreferences.allergies = response.body.allergies;
            this.userpreferences.cookingTime = response.body.cookingTime;
            this.userpreferences.userHeight = response.body.userHeight;
            this.userpreferences.userWeight = response.body.userWeight;
            this.userpreferences.userBMI = response.body.userBMI;
            this.userpreferences.bmiset = response.body.bmiset;
            this.userpreferences.cookingTimeSet = response.body.cookingTimeSet;
            this.userpreferences.allergiesSet = response.body.allergiesSet;
            this.userpreferences.macroSet = response.body.macroSet;
            this.userpreferences.budgetSet = response.body.budgetSet;
            this.userpreferences.calorieSet = response.body.calorieSet;
            this.userpreferences.foodPreferenceSet = response.body.foodPreferenceSet;
            this.userpreferences.shoppingIntervalSet = response.body.shoppingIntervalSet;
            this.userpreferences.macroRatio.fat = response.body.macroRatio.fat;
            this.userpreferences.macroRatio.carbs = response.body.macroRatio.carbs;
            this.userpreferences.macroRatio.protein = response.body.macroRatio.protein;
            console.log(this.userpreferences)
            this.updateDisplayData();
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          console.log('Unauthorized access. Please login again.', err);
          this.router.navigate(['../']);
        } else {
          console.log('Error loading user settings', err);
        }
       
      },
    });

   
  }

  setGoal() {
    this.updateSettingsOnServer(); // Update the settings on the server when the goal is set
  }
  

  private updateSettingsOnServer() {
    this.settingsApiService.updateSettings(this.userpreferences).subscribe(
      (response) => {
        if (response.status === 200) {
          // Successfully updated settings on the server
          console.log('Settings updated successfully');
        }
      },
      (error) => {
        // Handle error while updating settings
        console.log('Error updating settings', error);
      }
    );
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
    if (this.userpreferences.foodPreferenceSet === true) {
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
    } else if (this.userpreferences.foodPreferenceSet === false) {
      this.userpreferences.foodPreferences = [];
      this.displayPreferences = '';
      this.isPreferencesModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  preference_Toggle() {
    this.userpreferences.foodPreferenceSet = !this.userpreferences.foodPreferenceSet;
    this.updateSettingsOnServer();
  }

  getSelectedPreferences(): string {
    const selectedPreferences = [];
    if (this.userpreferences.foodPreferences == null) {
      this.userpreferences.foodPreferences = [];
      return '';
    }
    else
    {
      if (this.preferences.vegetarian) {
        selectedPreferences.push('Vegetarian');
        this.userpreferences.foodPreferences.push('Vegetarian');
      }
      if (this.preferences.vegan) {
        selectedPreferences.push('Vegan');
        this.userpreferences.foodPreferences.push('Vegan');
      }
      if (this.preferences.glutenIntolerant) {
        selectedPreferences.push('Gluten-intolerant');
        this.userpreferences.foodPreferences.push('Gluten-intolerant');
      }
      if (this.preferences.lactoseIntolerant) {
        selectedPreferences.push('Lactose-intolerant');
        this.userpreferences.foodPreferences.push('Lactose-intolerant');
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
    if (this.userpreferences.calorieSet === true) {
      if (this.userpreferences.calorieAmount) {
        if (!isOpen) {
          this.updateDisplayData(); // Update the display data when the modal is closed
        }
        this.isCalorieModalOpen = isOpen;
      }
    } else if (this.userpreferences.calorieSet === false) {
      this.userpreferences.calorieAmount = 0;
      this.isCalorieModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  calorieAmount_Toggle() {
    this.userpreferences.calorieSet = !this.userpreferences.calorieSet;
    this.updateSettingsOnServer();
  }

  showSelectedCalorieAmount(event: any) {
    this.userpreferences.calorieAmount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }

  setOpenBudgetSave(isOpen: boolean) {
    if (this.userpreferences.budgetSet === true) {
      this.userpreferences.budgetRange = this.selectedPriceRange;
      this.isBudgetModalOpen = isOpen;
    } else if (this.userpreferences.budgetSet === false) {
      this.userpreferences.budgetRange = '';
      this.isBudgetModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  budgetRange_Toggle() {
    this.userpreferences.budgetSet = !this.userpreferences.budgetSet;
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
          selectedIndex: 0, // Set the default selected index
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
          selectedIndex: 0, // Set the default selected index
        },
        {
          name: 'fats',
          options: [
            { text: '1', value: 1 },
            { text: '2', value: 2 },
            { text: '3', value: 3 },
            { text: '4', value: 4 },
            { text: '5', value: 5 },
          ],
          selectedIndex: 0, // Set the default selected index
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
            this.userpreferences.macroRatio.protein = value['protein'].value;
            this.userpreferences.macroRatio.carbs = value['carbs'].value;
            this.userpreferences.macroRatio.fat = value['fats'].value;
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
    if (this.userpreferences.macroSet === true) {
      if (!isOpen) {
        this.displaying_Macroratio = this.getDisplayMacroratio(); // Update the display data when the modal is closed
      }
      this.isMacroModalOpen = isOpen;
    } else if (this.userpreferences.macroSet === false) {
      this.userpreferences.macroRatio.protein = 0;
      this.userpreferences.macroRatio.carbs = 0;
      this.userpreferences.macroRatio.fat = 0;
      this.displaying_Macroratio = '';
      this.isMacroModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  macro_Toggle() {
    this.userpreferences.macroSet = !this.userpreferences.macroSet;
    this.updateSettingsOnServer();
  }

  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
  }

  setOpenAllergiesSave(isOpen: boolean) {
    if (this.userpreferences.allergiesSet === true) {
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
    } else if (this.userpreferences.allergiesSet === false) {
      this.userpreferences.allergies = [];
      this.displayAllergies = '';
      this.isAllergiesModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  allergies_Toggle() {
    this.userpreferences.allergiesSet = !this.userpreferences.allergiesSet;
    this.updateSettingsOnServer();
  }

  getSelectedAllergens(): string {

    if (this.userpreferences.allergies == null) {
      this.userpreferences.allergies = [];
      return '';
    }
    else
    {
    const selectedAllergens = [];

    if (this.allergens.seafood) {
      selectedAllergens.push('Seafood');
      this.userpreferences.allergies.push('Seafood');
    }
    if (this.allergens.nuts) {
      selectedAllergens.push('Nuts');
      this.userpreferences.allergies.push('Nuts');
    }
    if (this.allergens.eggs) {
      selectedAllergens.push('Eggs');
      this.userpreferences.allergies.push('Eggs');
    }
    if (this.allergens.soy) {
      selectedAllergens.push('Soy');
      this.userpreferences.allergies.push('Soy');
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
    if (this.userpreferences.cookingTimeSet === true) {
      this.isCookingModalOpen = isOpen;
    } else if (this.userpreferences.cookingTimeSet === false) {
      this.userpreferences.cookingTime = 0;
      this.isCookingModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  cookingtime_Toggle() {
    this.userpreferences.cookingTimeSet = !this.userpreferences.cookingTimeSet;
    this.updateSettingsOnServer();
  }

  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }

  setOpenBMISave(isOpen: boolean) {
    if (this.userpreferences.bmiset === true) {
      if (!isOpen) {
        this.calculateBMI(); // Call BMI calculation function and update the display data
      }
    } else if (this.userpreferences.bmiset === false) {
      this.userpreferences.userBMI = 0;
      this.isBMIModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  BMI_Toggle() {
    this.userpreferences.bmiset = !this.userpreferences.bmiset;
    this.updateSettingsOnServer();
  }

  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.userpreferences.shoppingIntervalSet === true) {
      if (this.shoppingInterval === 'other') {
        this.userpreferences.shoppingInterval = this.shoppingIntervalOtherValue.toString();
      } else if (
        this.shoppingInterval == 'weekly' ||
        this.shoppingInterval == 'biweekly' ||
        this.shoppingInterval == 'monthly'
      ) {
        this.userpreferences.shoppingInterval = this.shoppingInterval;
      }
      this.isShoppingModalOpen = isOpen;
    } else if (this.userpreferences.shoppingIntervalSet === false) {
      this.userpreferences.shoppingInterval = '';
      this.isShoppingModalOpen = isOpen;
    }
    this.updateSettingsOnServer();
  }

  shoppingInterval_Toggle() {
    this.userpreferences.shoppingIntervalSet = !this.userpreferences.shoppingIntervalSet;
    this.updateSettingsOnServer();
  }

  // Function to update display data
  updateDisplayData() {
    this.displayPreferences = this.getSelectedPreferences();
    this.displaying_Macroratio = this.getDisplayMacroratio();
    this.displayAllergies = this.getSelectedAllergens();
  }

  // Function to get the displaying macro ratio
  getDisplayMacroratio(): string {
    console.log(this.userpreferences.macroRatio)
    if(this.userpreferences && this.userpreferences.macroRatio){
        return (
            this.userpreferences.macroRatio.protein +
            ' : ' +
            this.userpreferences.macroRatio.carbs +
            ' : ' +
            this.userpreferences.macroRatio.fat
        );
    } else {
        return "Not available";
    }
}


  // Function to calculate BMI
  calculateBMI() {
    // Implement your BMI calculation logic here and update the userpreferences.userBMI accordingly
  }
}
