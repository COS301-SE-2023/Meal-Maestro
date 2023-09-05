import { Component, OnInit } from '@angular/core';
import { IonicModule, PickerController, ToastController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { UserPreferencesI } from '../../models/userpreference.model';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SettingsApiService } from '../../services/settings-api/settings-api.service';

import { UserI } from '../../models/user.model';
import { AuthenticationService } from '../../services/services';


@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule],
})

export class ProfilePage implements OnInit {
  

  constructor(
    private router: Router,
    private pickerController: PickerController,
    private settingsApiService: SettingsApiService,
    private auth: AuthenticationService,
    private toastController: ToastController
  ) {
   
  }
  // User data
  user: UserI = {
    username: '',
    email: '',
    password: '',
  };

  userpreferences: UserPreferencesI = {
    goal: '',
    shoppingInterval: '',
  foodPreferences: [],
    calorieAmount: 0,
    budgetRange: '',
    macroRatio: { protein: 0, carbs: 0, fat: 0 },
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
  shoppingIntervalOtherValue: number | undefined | any = 7;
  shoppingInterval: string | any;
  displayAllergies: string[] | string = '';
  displayPreferences: string[] | string = '' ;
  selectedPreferences: string | any;
  selectedPriceRange: string | any;

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
  initialshoppinginterval : string | any;
  initialpreference : string | any;
  initialpreferenceVegetarian : string | any;
  initialpreferenceVegan : string | any;
  initialpreferenceGlutenIntolerant : string | any;
  initialpreferenceLactoseIntolerant : string | any;
  
  initialcalorie : number | any;
  initialbudget : string | any;
  initialmacro : any;
  initialallergies : string | any;
  initialcooking : string | any;
  initialBMI : number | any;
  initialshoppingintervalToggle : boolean | any;
  initialpreferenceToggle : boolean | any;
  initialcalorieToggle : boolean | any;
  initialbudgetToggle : boolean | any;
  initialmacroToggle : boolean | any;
  initialallergiesToggle : boolean | any;
  initialcookingToggle : boolean | any;
  initialBMIToggle : boolean | any;

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
        console.log(error);
      }
    })
  }

   private async loadUserSettings() {
    this.settingsApiService.getSettings().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) { 
            console.log("loaduser")
            console.log(response.body)
            this.userpreferences.goal = response.body.goal;
            if (response.body.shoppingInterval != '') {
              this.userpreferences.shoppingIntervalSet = true;
            }

            if (response.body.shoppingInterval === 'weekly' || response.body.shoppingInterval === 'biweekly' || response.body.shoppingInterval === 'monthly') {
              this.userpreferences.shoppingInterval = response.body.shoppingInterval;
              this.shoppingInterval = response.body.shoppingInterval;
            }
            else if (response.body.shoppingInterval.includes("days")) {
              this.userpreferences.shoppingInterval = "other";  
              this.shoppingIntervalOtherValue = response.body.shoppingInterval;
            }
            else {
              this.userpreferences.shoppingIntervalSet = false;
              this.userpreferences.shoppingInterval = '';
              this.shoppingInterval = ''; 
            }

            this.userpreferences.foodPreferences = response.body.foodPreferences;
            if (response.body.calorieAmount == 0) {
              this.userpreferences.calorieAmount = '';
            }
            else 
            {
            this.userpreferences.calorieAmount = response.body.calorieAmount;
            }

            console.log("budget")
            console.log(response.body.budgetRange)
            if (response.body.budgetRange.includes("R")) {
              this.userpreferences.budgetRange = response.body.budgetRange;
              this.selectedPriceRange = "custom";
            }
            else
            {
              this.userpreferences.budgetRange = response.body.budgetRange;
              this.selectedPriceRange = response.body.budgetRange;
            }
            this.userpreferences.allergies = response.body.allergies;
            this.userpreferences.cookingTime = response.body.cookingTime;
            this.userpreferences.userHeight = response.body.userHeight;
            this.userpreferences.userWeight = response.body.userWeight;
            if (response.body.userBMI == 0) {
              this.userpreferences.userBMI = '';
            }
            else
            this.userpreferences.userBMI = response.body.userBMI;
            this.userpreferences.bmiset = response.body.bmiset;
            this.userpreferences.cookingTimeSet = response.body.cookingTimeSet;
            this.userpreferences.allergiesSet = response.body.allergiesSet;
            if (response.body.macroRatio.protein > 0 && response.body.macroRatio.carbs > 0 && response.body.macroRatio.fat > 0 && response.body.macroSet === true)
            {
              this.userpreferences.macroSet = true;
            }
            else if (response.body.macroRatio.protein === 0 || response.body.macroRatio.carbs === 0 || response.body.macroRatio.fat === 0 || response.body.macroSet === false)
           {
                this.userpreferences.macroSet = false;
            }
            this.userpreferences.budgetSet = response.body.budgetSet;
            this.userpreferences.calorieSet = response.body.calorieSet;
            this.userpreferences.shoppingIntervalSet = response.body.shoppingIntervalSet;
            this.userpreferences.macroRatio.fat = response.body.macroRatio.fat;
            this.userpreferences.macroRatio.carbs = response.body.macroRatio.carbs;
            this.userpreferences.macroRatio.protein = response.body.macroRatio.protein;
            this.displayPreferences = this.userpreferences.foodPreferences;
            this.displayAllergies = this.userpreferences.allergies;

            this.shoppingintervalToggle = this.userpreferences.shoppingIntervalSet;
            this.calorieToggle = this.userpreferences.calorieSet;
            this.budgetToggle = this.userpreferences.budgetSet;
           
            this.allergiesToggle = this.userpreferences.allergiesSet;
            this.cookingToggle = this.userpreferences.cookingTimeSet;
            this.BMIToggle = this.userpreferences.bmiset;
            this.shoppingInterval = this.userpreferences.shoppingInterval;
            
            
            this.displaying_Macroratio = this.getDisplayMacroratio();
            this.updateDisplayData();

            this.setInitialAllergies()
            this.setInitialBMI()
            this.setInitialBudget()
            this.setInitialCalorie()
            this.setInitialCooking()
            this.setIntialPreference()
            this.setInitialMacro()
            this.setInitialShopping()
            this.setInitialBMI();

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
    console.log("update")
    //if check to ensure only 1 "days" is in the string 
    if (this.userpreferences.shoppingInterval.includes("days") && this.userpreferences.shoppingInterval.split("days").length - 1 > 1) {
      this.userpreferences.shoppingInterval = this.userpreferences.shoppingInterval.replace("days", "").trim();
    }

    console.log(this.userpreferences)

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
    this.resetPreference();
  }

  setOpenPreferencesSave(isOpen: boolean) {
    console.log('Saving Preferences');
    if (this.userpreferences.foodPreferenceSet === true) {
      this.getSelectedPreferences();  // This will update this.userpreferences.foodPreferences
      if (!isOpen) {
        this.updateDisplayData(); // Update the display data when the modal is closed
      }
      this.isPreferencesModalOpen = isOpen;
    } else {
      this.userpreferences.foodPreferences = [];
      this.displayPreferences = '';
      this.isPreferencesModalOpen = isOpen;
    }
    this.setIntialPreference();
    this.updateSettingsOnServer();
  }

  preference_Toggle() {
    this.userpreferences.foodPreferenceSet = !this.userpreferences.foodPreferenceSet;
  }

  getSelectedPreferences(): string {
    const selectedPreferences = [];
    if (this.userpreferences.foodPreferences == null) {
      this.userpreferences.foodPreferences = [];
      return '';
    }
    else
    {
      this.userpreferences.foodPreferences = [];
      if (this.preferences.vegetarian && !this.userpreferences.foodPreferences.includes('Vegetarian')) {
        console.log("here")
        selectedPreferences.push('Vegetarian');
        this.userpreferences.foodPreferences.push('Vegetarian');
      }
      if (this.preferences.vegan && !this.userpreferences.foodPreferences.includes('Vegan')) {
        selectedPreferences.push('Vegan');
        this.userpreferences.foodPreferences.push('Vegan');
      }
      if (this.preferences.glutenIntolerant && !this.userpreferences.foodPreferences.includes('Gluten-intolerant')) {
        selectedPreferences.push('Gluten-intolerant');
        this.userpreferences.foodPreferences.push('Gluten-intolerant');
      }
      if (this.preferences.lactoseIntolerant && !this.userpreferences.foodPreferences.includes('Lactose-intolerant')) {
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
    this.resetCalorie();
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
      this.userpreferences.calorieAmount = '';
      this.isCalorieModalOpen = isOpen;
    }
    this.setInitialCalorie();
    this.updateSettingsOnServer();
    
  }

  calorieAmount_Toggle() {
    this.userpreferences.calorieSet = !this.userpreferences.calorieSet;
  }

  showSelectedCalorieAmount(event: any) {
    this.userpreferences.calorieAmount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
    this.resetBudget();
  }

  setOpenBudgetSave(isOpen: boolean) {
    console.log('setOpenBudgetSave called with:', isOpen); // Debug 1
  
    if (this.userpreferences.budgetSet === true) {
      console.log('Budget is set.'); // Debug 2
  
      if (this.selectedPriceRange === 'custom') {
        console.log('Custom range selected.'); // Debug 3
  
        if (this.userpreferences.budgetRange !== null && this.userpreferences.budgetRange !== undefined) {
          console.log('Budget range is neither null nor undefined.'); // Debug 4
  
          const budgetString = this.userpreferences.budgetRange.toString();
          const rCount = (budgetString.match(/R/g) || []).length;
  
          const isValid = /^[R]?[0-9\s]*$/.test(budgetString);
  
          if (!isValid) {
            this.userpreferences.budgetRange = budgetString.replace(/[^0-9R\s]/g, '');
          }
  
          if (rCount > 1) {
            this.userpreferences.budgetRange = budgetString.replace(/R/g, '').trim();
            this.userpreferences.budgetRange = 'R ' + this.userpreferences.budgetRange;
          } else if (rCount === 0) {
            this.userpreferences.budgetRange = 'R ' + budgetString;
          }
        }
      } else {
        this.userpreferences.budgetRange = this.selectedPriceRange;
      }
  
      this.isBudgetModalOpen = false;
      console.log('Attempting to close the modal.'); // Debug 5
    } else {
      this.userpreferences.budgetRange = '';
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
    this.userpreferences.budgetSet = !this.userpreferences.budgetSet;
    if (!this.userpreferences.budgetSet && this.selectedPriceRange === 'custom') {
      this.userpreferences.budgetRange = '';
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
          selectedIndex: this.userpreferences.macroRatio.protein, // Set the default selected index
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
          selectedIndex: this.userpreferences.macroRatio.carbs, // Set the default selected index
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
          selectedIndex: this.userpreferences.macroRatio.fat, // Set the default selected index
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
            this.userpreferences.macroRatio.fat = value['fat'].value;
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

    if (this.userpreferences.macroSet === true) {
      if (!isOpen) {
        this.displaying_Macroratio = this.getDisplayMacroratio();
      }
     
      this.isMacroModalOpen = isOpen;
      
    } else if (this.userpreferences.macroSet === false) {
      this.userpreferences.macroRatio.protein = 0;
      this.userpreferences.macroRatio.carbs = 0;
      this.userpreferences.macroRatio.fat = 0;
      this.displaying_Macroratio = '';
      this.isMacroModalOpen = isOpen;
    }
    this.setInitialMacro();
    this.updateSettingsOnServer();
  }
  macro_Toggle() {
    this.userpreferences.macroSet = !this.userpreferences.macroSet;
  }
  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
    this.resetAllergies();
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
    this.setInitialAllergies();
    this.updateSettingsOnServer();
  }
  allergies_Toggle() {
    this.userpreferences.allergiesSet = !this.userpreferences.allergiesSet;
  }
  getSelectedAllergens(): string {
    const selectedAllergens  = [];
    if (this.userpreferences.allergies == null) {
      this.userpreferences.allergies = [];
      return '';
    }
    else
    {
     this.userpreferences.allergies = [];

    if (this.allergens.seafood && !this.userpreferences.allergies.includes('Seafood')) {
      selectedAllergens.push('Seafood');
      this.userpreferences.allergies.push('Seafood');
    }
    if (this.allergens.nuts && !this.userpreferences.allergies.includes('Nuts')) {
      selectedAllergens.push('Nuts');
      this.userpreferences.allergies.push('Nuts');
    }
    if (this.allergens.eggs && !this.userpreferences.allergies.includes('Eggs')) {
      selectedAllergens.push('Eggs');
      this.userpreferences.allergies.push('Eggs');
    }
    if (this.allergens.soy && !this.userpreferences.allergies.includes('Soy')) {
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
    this.resetCooking();
    this.isCookingModalOpen = isOpen;
  }
  setOpenCookingSave(isOpen: boolean) {
    if (this.userpreferences.cookingTimeSet === true) {
      this.isCookingModalOpen = isOpen;
    } else if (this.userpreferences.cookingTimeSet === false) {
      this.userpreferences.cookingTime = '';
      this.isCookingModalOpen = isOpen;
    }
    this.setInitialCooking();
    this.updateSettingsOnServer();
  }
  cookingtime_Toggle() {
    this.userpreferences.cookingTimeSet = !this.userpreferences.cookingTimeSet;
  }
  setOpenBMI(isOpen: boolean) {
    this.resetBMI();
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {
    if (this.userpreferences.bmiset === true && this.userpreferences.userHeight > 0 && this.userpreferences.userWeight > 0) {
     if (this.userpreferences.userHeight > 0 && this.userpreferences.userWeight > 0) {
      this.calculateBMI();
      this.updateDisplayData();
      this.updateSettingsOnServer();
      this.isBMIModalOpen = isOpen;
     }
    } 
     if (this.userpreferences.bmiset === false) {
      this.userpreferences.userHeight = 0;
      this.userpreferences.userWeight = 0;
      this.userpreferences.userBMI = '';
      this.isBMIModalOpen = isOpen;
     }
    this.setInitialBMI();
    this.updateSettingsOnServer();
    
}

  BMI_Toggle() {
    this.userpreferences.bmiset = !this.userpreferences.bmiset;
  }

  setOpenShopping(isOpen: boolean) {
    if (isOpen === false) {
      console.log("resetClose")
      console.log(this.userpreferences.shoppingInterval)
    this.resetShopping()
    console.log(this.userpreferences.shoppingInterval)
    this.isShoppingModalOpen = isOpen;
    }
    else if (isOpen === true) {
      console.log("resetOpen")
      if (this.userpreferences.shoppingInterval.includes("days")) {
        this.shoppingInterval = "other";
        this.shoppingIntervalOtherValue = this.userpreferences.shoppingInterval.replace("days", "").trim();
      }
      console.log(this.userpreferences.shoppingInterval)
     
    this.isShoppingModalOpen = isOpen;
    }
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.userpreferences.shoppingIntervalSet === true) {
      if (this.shoppingInterval === 'other') {
  
        this.userpreferences.shoppingInterval = this.shoppingIntervalOtherValue.toString() + " days";
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
    else if (this.userpreferences.shoppingIntervalSet === false && this.shoppingInterval === 'other') {
      this.userpreferences.shoppingInterval = '';
      this.isShoppingModalOpen = isOpen;
    }
    this.setInitialShopping();
    this.updateSettingsOnServer();
  }

  shoppingInterval_Toggle() {
    this.userpreferences.shoppingIntervalSet = !this.userpreferences.shoppingIntervalSet;
    if (this.userpreferences.shoppingIntervalSet === false) {
      this.shoppingInterval = '';
    }
  }

  // Function to determine what to display for Shopping Interval
getDisplayShoppingInterval() {
  if (this.shoppingInterval === 'other') {
    if (this.shoppingIntervalOtherValue.toString().includes("days")) {
    return this.shoppingIntervalOtherValue ;
    }
    else 
    {
      return this.shoppingIntervalOtherValue + " days";
    }
  } else {
    return this.userpreferences.shoppingInterval;
  }
}

getDisplayOtherShoppingInterval() {

  if (this.shoppingIntervalOtherValue.toString().includes("days")) {
    return this.shoppingIntervalOtherValue.toString().replace("days", "").trim();
  } else {
    return this.shoppingIntervalOtherValue;
  }
}




  // Function to update display data
  updateDisplayData() {
    if (this.userpreferences.shoppingInterval != '') {
    this.shoppingintervalToggle = true
    this.shoppingInterval = this.userpreferences.shoppingInterval;
    this.userpreferences.shoppingIntervalSet = true;
    }

    if (this.userpreferences.foodPreferences != null && this.userpreferences.foodPreferences.length != 0) {
      this.preferenceToggle = true
      if (this.userpreferences.foodPreferences.includes('Vegetarian')) {
        this.preferences.vegetarian = true;
      }
      if (this.userpreferences.foodPreferences.includes('Vegan')) {
        this.preferences.vegan = true;
      }
      if (this.userpreferences.foodPreferences.includes('Gluten-intolerant')) {
        this.preferences.glutenIntolerant = true;
      }
      if (this.userpreferences.foodPreferences.includes('Lactose-intolerant')) {
        this.preferences.lactoseIntolerant = true;
      }
      this.userpreferences.foodPreferenceSet = true;

      }

      console.log("budgetupdatedisplay");
      console.log(this.userpreferences.budgetRange);
      
      // Convert budgetRange to a string to avoid type errors
      const budgetString = this.userpreferences.budgetRange ? this.userpreferences.budgetRange.toString() : '';
      
      // Check for the presence of 'R' and whether it's a custom range
      if (budgetString.includes("R") && ['low', 'moderate', 'high'].indexOf(budgetString.toLowerCase().replace('r ', '')) === -1) {
        this.budgetToggle = true;
        this.selectedPriceRange = "custom";
        this.userpreferences.budgetSet = true;
        console.log("budget- custom");
      }
      else {
        this.budgetToggle = true;
        this.selectedPriceRange = budgetString.replace('R ', '');
        this.userpreferences.budgetSet = true;
        console.log("budget- not custom");
      }


    if (this.userpreferences.allergies != null && this.userpreferences.allergies.length != 0) {
        this.allergiesToggle = true
        if (this.userpreferences.allergies.includes('Seafood')) {
          this.allergens.seafood = true;
        }
        if (this.userpreferences.allergies.includes('Nuts')) {
          this.allergens.nuts = true;
        }
        if (this.userpreferences.allergies.includes('Eggs')) {
          this.allergens.eggs = true;
        }
        if (this.userpreferences.allergies.includes('Soy')) {
          this.allergens.soy = true;
        }
        this.userpreferences.allergiesSet = true;
      }

      if (this.userpreferences.userBMI != 0) {
        this.BMIToggle = true
        this.userpreferences.bmiset = true;
        }

      if (this.userpreferences.cookingTime != '') {
        this.cookingToggle = true
        this.userpreferences.cookingTimeSet = true;
        }
 
    
    this.displayPreferences = this.getSelectedPreferences();
    this.displaying_Macroratio = this.getDisplayMacroratio();
    this.displayAllergies = this.getSelectedAllergens();
  }

  // Function to get the displaying macro ratio
  getDisplayMacroratio(): string {
    if(this.userpreferences.macroSet && this.userpreferences.macroRatio && this.userpreferences.macroRatio.protein > 0 && this.userpreferences.macroRatio.carbs > 0 && this.userpreferences.macroRatio.fat > 0){
        return (
            this.userpreferences.macroRatio.protein +
            ' : ' +
            this.userpreferences.macroRatio.carbs +
            ' : ' +
            this.userpreferences.macroRatio.fat
        );
    } else {
        return "";
    }
}

calculateBMI() {
    this.userpreferences.userBMI = Math.round(this.userpreferences.userWeight /
        this.userpreferences.userHeight*this.userpreferences.userHeight);
 }


 setInitialShopping()
 {
    this.initialshoppinginterval = this.userpreferences.shoppingInterval;
    this.initialshoppingintervalToggle = this.userpreferences.shoppingIntervalSet;
 }

 setIntialPreference() {
  console.log('Setting Initial Preferences');
  this.initialpreference = this.userpreferences.foodPreferences;
  this.initialpreferenceToggle = this.userpreferences.foodPreferenceSet;
  this.initialpreferenceVegetarian = this.preferences.vegetarian;
  this.initialpreferenceVegan = this.preferences.vegan;
  this.initialpreferenceGlutenIntolerant = this.preferences.glutenIntolerant;
  this.initialpreferenceLactoseIntolerant = this.preferences.lactoseIntolerant;
}

  setInitialCalorie()
  {
    this.initialcalorie = this.userpreferences.calorieAmount;
    this.initialcalorieToggle = this.userpreferences.calorieSet;
  }

  setInitialBudget()
  {
    this.initialbudget = this.userpreferences.budgetRange;
    this.initialbudgetToggle = this.userpreferences.budgetSet;
  }

  setInitialMacro()
  {
    this.initialmacro = this.userpreferences.macroRatio;
    this.initialmacroToggle = this.userpreferences.macroSet;
  }

  setInitialAllergies()
  {
    this.initialallergies = this.userpreferences.allergies;
    this.initialallergiesToggle = this.userpreferences.allergiesSet;
  }

  setInitialCooking()
  {
    this.initialcooking = this.userpreferences.cookingTime;
    this.initialcookingToggle = this.userpreferences.cookingTimeSet;
  }

  setInitialBMI()
  {
    this.initialBMI = this.userpreferences.userBMI;
    this.initialBMIToggle = this.userpreferences.bmiset;
  }


 resetShopping()
{
  if (this.userpreferences.shoppingInterval.includes("days")) {
     const temp = this.userpreferences.shoppingInterval;
    this.userpreferences.shoppingInterval = "other";  
    this.shoppingIntervalOtherValue = temp;
  }

 
  this.userpreferences.shoppingInterval = this.initialshoppinginterval;
  this.shoppingintervalToggle = this.initialshoppingintervalToggle;
  this.userpreferences.shoppingIntervalSet = this.initialshoppingintervalToggle;
  this.shoppingInterval = this.initialshoppinginterval;
}

resetPreference() {
  console.log('Resetting Preferences');
  // Reset both userpreferences and preferences objects
  this.preferences.vegetarian = this.initialpreferenceVegetarian;
  this.preferences.vegan = this.initialpreferenceVegan;
  this.preferences.glutenIntolerant = this.initialpreferenceGlutenIntolerant;
  this.preferences.lactoseIntolerant = this.initialpreferenceLactoseIntolerant;

  this.userpreferences.foodPreferences = this.initialpreference;
  this.userpreferences.foodPreferenceSet = this.initialpreferenceToggle;
  this.preferenceToggle = this.initialpreferenceToggle;
  this.displayPreferences = this.initialpreference;
}

resetCalorie()
{
  this.userpreferences.calorieAmount = this.initialcalorie;
  this.calorieToggle = this.initialcalorieToggle;
  this.userpreferences.calorieSet = this.initialcalorieToggle;
}

resetBudget()
{
  console.log("resetbudget")
  if (this.initialbudget.includes("R") && this.initialbudgetToggle === true) {
   this.userpreferences.budgetRange = this.initialbudget;
   this.selectedPriceRange = "custom";
   this.userpreferences.budgetSet = this.initialbudgetToggle;
   this.budgetToggle = this.initialbudgetToggle;
  }
  else if (!this.initialbudget.includes("R") && this.initialbudgetToggle === true)
  {
    this.userpreferences.budgetRange = this.initialbudget;
    this.selectedPriceRange = this.initialbudget;
    this.userpreferences.budgetSet = this.initialbudgetToggle;
  this.budgetToggle = this.initialbudgetToggle;
  }
  
}

resetMacro()
{
  this.userpreferences.macroRatio = this.initialmacro;
  this.userpreferences.macroSet = this.initialmacroToggle;
  this.macroToggle = this.initialmacroToggle;
  this.displaying_Macroratio = this.getDisplayMacroratio();
}

resetAllergies()
{
  this.userpreferences.allergies = this.initialallergies;
  this.userpreferences.allergiesSet = this.initialallergiesToggle;
  this.allergiesToggle = this.initialallergiesToggle;
  this.displayAllergies = this.initialallergies;
}

resetCooking()
{
  this.userpreferences.cookingTime = this.initialcooking;
  this.userpreferences.cookingTimeSet = this.initialcookingToggle;
  this.cookingToggle = this.initialcookingToggle;
}

resetBMI()
{
  this.userpreferences.userBMI = this.initialBMI;
  this.userpreferences.bmiset = this.initialBMIToggle;
  this.BMIToggle = this.initialBMIToggle;
}



disabledConfirmShopping(): boolean {
  if (this.userpreferences.shoppingIntervalSet) {
    return !this.shoppingInterval; 
  }
  return false; 
}

disabledConfirmPreference(): boolean {
  if (this.userpreferences.foodPreferenceSet) {
    return !this.displayPreferences; 
  }
  return false; 
}

disabledConfirmBudget(): boolean {


  if (this.userpreferences.budgetSet) {
    return !this.selectedPriceRange; 
  }
  return false; 
}

disabledCalorieCookingTime(): boolean {
  if (this.userpreferences.cookingTimeSet) {
    return !this.userpreferences.cookingTime; 
  }
  return false; 
}

async presentToast(message: string) {
  const toast = await this.toastController.create({
    message: message,
    duration: 2000,
    position: 'middle',
  });
  toast.present();
}


}

