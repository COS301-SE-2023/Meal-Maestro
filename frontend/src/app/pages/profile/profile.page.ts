import { Component } from '@angular/core';
import { IonicModule, PickerController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { UserPreferencesI } from '../../models/userpreferance.model';

import { CommonModule } from '@angular/common';
import { RangeCustomEvent, RangeValue } from '@ionic/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule],
})
export class ProfilePage {
  constructor( private router: Router , private pickerController: PickerController ) {this.selectedPriceRange = '';
}
  userpreferances : UserPreferencesI = {
    goal: '',
    shopping_interval: '',
    food_preferences: [],
    calorie_amount: 0,
    budget_range: '',
    macro_ratio: {protien: 0, carbs: 0, fats: 0},
    allergies: [],
    cooking_time: 0,
    user_height: 0,
    user_weight: 0,
    user_BMI: 0
  };
  //Variables for displaying
  displaying_Macroratio: string | undefined;
  shoppingIntervalOtherValue: number | undefined | any;
  shopping_interval: string | any;
  displayAllergies: string = '';
  displayPreferences: string = '';
  selectedPreferences: string | any;
  selectedPriceRange: string;
 
  //check if possible to change
  preferences = {
    vegetarian: false,
    vegan: false,
    glutenIntolerant: false,
    lactoseIntolerant: false
  };
  allergens = {
    nuts: false,
    seafood: false,
    soy: false,
    eggs: false
  };

  //modal controllers
  isPreferencesModalOpen: boolean = false;
  isCalorieModalOpen: boolean = false;
  isBudgetModalOpen: boolean = false;
  isMacroModalOpen: boolean = false;
  isAllergiesModalOpen: boolean = false;
  isCookingModalOpen: boolean = false;
  isBMIModalOpen: boolean = false;
  isShoppingModalOpen: boolean = false;


  //function to navigate to account-profile page
  navToProfile(){
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
  
      
    if (this.preferences.vegetarian || this.preferences.vegan || this.preferences.glutenIntolerant || this.preferences.lactoseIntolerant) {
      if (!isOpen) {
        this.displayPreferences = this.getSelectedPreferences();
      }
      this.isPreferencesModalOpen = isOpen;
    }
  }
  
  getSelectedPreferences(): string {
    const selectedPreferences = [];
  
    if (this.preferences.vegetarian) {
      selectedPreferences.push('Vegetarian');
      this.userpreferances.food_preferences.push('Vegetarian');
    }
    if (this.preferences.vegan) {
      selectedPreferences.push('Vegan');
      this.userpreferances.food_preferences.push('Vegan');
    }
    if (this.preferences.glutenIntolerant) {
      selectedPreferences.push('Gluten-intolerant');
      this.userpreferances.food_preferences.push('Gluten-intolerant');
    }
    if (this.preferences.lactoseIntolerant) {
      selectedPreferences.push('Lactose-intolerant');
      this.userpreferances.food_preferences.push('Lactose-intolerant');
    }
  
    if (selectedPreferences.length === 1) {
      return selectedPreferences[0];
    } else if (selectedPreferences.length > 1) {
      return 'Multiple';
    } else {
      return '';
    
    }
    
  }

  setOpenCalorie(isOpen: boolean) {
    this.isCalorieModalOpen = isOpen;
  }
  setOpenCalorieSave(isOpen: boolean) {
    if (this.userpreferances.calorie_amount) {
    if (!isOpen) {
    }
    this.isCalorieModalOpen = isOpen;
  }

  }
  showSelectedCalorieAmount(event: any) {
    this.userpreferances.calorie_amount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }
  setOpenBudgetSave(isOpen: boolean) {
    this.userpreferances.budget_range = this.selectedPriceRange;
      this.isBudgetModalOpen = isOpen;
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
            this.userpreferances.macro_ratio.protien = value['protein'].value;
            this.userpreferances.macro_ratio.carbs = value['carbs'].value;
            this.userpreferances.macro_ratio.fats = value['fats'].value;
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
    if (!isOpen) {
      this.displaying_Macroratio = this.userpreferances.macro_ratio.protien + ' : ' + this.userpreferances.macro_ratio.carbs + ' : ' + this.userpreferances.macro_ratio.fats;
    }
    this.isMacroModalOpen = isOpen;
    
  }
  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
  }
  setOpenAllergiesSave(isOpen: boolean) {

    if (this.allergens.seafood || this.allergens.nuts || this.allergens.eggs || this.allergens.soy) {
      
        if (!isOpen) {
          this.displayAllergies = this.getSelectedAllergens();
        }
    this.isAllergiesModalOpen = isOpen;
      }
    }
      getSelectedAllergens(): string {
        const selectedAllergens = [];
      
        if (this.allergens.seafood) {
          selectedAllergens.push('Seafood');
          this.userpreferances.allergies.push('Seafood')
        }
        if (this.allergens.nuts) {
          selectedAllergens.push('Nuts');
          this.userpreferances.allergies.push('Nuts')
        }
        if (this.allergens.eggs) {
          selectedAllergens.push('Eggs');
          this.userpreferances.allergies.push('Eggs')
        }
        if (this.allergens.soy) {
          selectedAllergens.push('Soy');
          this.userpreferances.allergies.push('Soy')
        }
      
        if (selectedAllergens.length === 1) {
          console.log(this.displayAllergies);
          return selectedAllergens[0];
        } else if (selectedAllergens.length > 1) {
          return 'Multiple';
        } else {
          console.log(this.displayAllergies);
          return '';
         
        }
        
      }
  setOpenCooking(isOpen: boolean) {
    this.isCookingModalOpen = isOpen;
  }
  setOpenCookingSave(isOpen: boolean) {
    this.isCookingModalOpen = isOpen;
  }

  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {
    //saving logic
    if (!isOpen) {
      //call BMI calculation fuction
    this.isBMIModalOpen = isOpen;
    }

  }
  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.shopping_interval === 'other') {
        this.userpreferances.shopping_interval = this.shoppingIntervalOtherValue.toString();
    }
      else if (this.shopping_interval == 'weekly' || this.shopping_interval == 'biweekly' || this.shopping_interval == 'monthly')
      {
        this.userpreferances.shopping_interval = this.shopping_interval;
      }
    this.isShoppingModalOpen = isOpen;
  }
}

 


