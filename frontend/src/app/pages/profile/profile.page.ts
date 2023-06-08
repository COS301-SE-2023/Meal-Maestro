import { Component } from '@angular/core';
import { IonicModule, PickerController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';

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
height: any;
weight: any;
  calorieAmount: number | undefined;
  cookingAmount: number | undefined;
selectedCookingTime: string | undefined;
  selectedBudgetRange: string | undefined;
  MacroRatio: string | undefined;
Allergies: any;
selectedBMICalculator: any;
 

  constructor( private router: Router , private pickerController: PickerController ) {this.selectedPriceRange = '';
  this.customAmount = 0;
}

selectedShoppingInterval: any;
shoppingInterval: any;
cookingTime: any;
customInterval: any;
protein: any;
carbs: any;
fats: any;
  selectedPriceRange: string;
  customAmount: number;
  lastEmittedValue: RangeValue | undefined; 
  anyChanges: boolean = false;
  message: string | undefined;
  selectedCalorieAmount: number | undefined;

  preferences = {
    vegetarian: false,
    vegan: false,
    glutenIntolerant: false,
    lactoseIntolerant: false
  };
  selectedPreferences: string = '';


  onIonChange(ev: Event) {
    this.lastEmittedValue = (ev as RangeCustomEvent).detail.value;
  }

  showSaveChanges(){
    this.anyChanges = true;
  }
  
  navToProfile(){
    this.router.navigate(['acc-profile']);
  }
  handlePriceRangeChange() {
    if (this.selectedPriceRange === 'custom') {
      // Perform any custom logic when the "Custom Amount" option is selected
    }
  }

  isPreferencesModalOpen: boolean = false;
  isCalorieModalOpen: boolean = false;
  isBudgetModalOpen: boolean = false;
  isMacroModalOpen: boolean = false;
  isAllergiesModalOpen: boolean = false;
  isCookingModalOpen: boolean = false;
  isBMIModalOpen: boolean = false;
  isShoppingModalOpen: boolean = false;

  setOpenPreferences(isOpen: boolean) {
    this.isPreferencesModalOpen = isOpen;
  }
  setOpenPreferencesSave(isOpen: boolean) {
    // saving logic
    if (isOpen) {
      // Update the selected preferences
      this.selectedPreferences = this.getSelectedPreferences();
    }
    this.isPreferencesModalOpen = false; // Corrected line
  }
  getSelectedPreferences(): string {
    const selectedPreferences = [];
  
    if (this.preferences.vegetarian) {
      selectedPreferences.push('Vegetarian');
    }
    if (this.preferences.vegan) {
      selectedPreferences.push('Vegan');
    }
    if (this.preferences.glutenIntolerant) {
      selectedPreferences.push('Gluten-intolerant');
    }
    if (this.preferences.lactoseIntolerant) {
      selectedPreferences.push('Lactose-intolerant');
    }
  
    return selectedPreferences.join(', ');
  }

  setOpenCalorie(isOpen: boolean) {
    this.isCalorieModalOpen = isOpen;
  }
  setOpenCalorieSave(isOpen: boolean) {
    //saving logic
    if (isOpen) {
      // Save the changes
      this.selectedCalorieAmount = this.calorieAmount;
    }
    this.isCalorieModalOpen = false;
  }
  showSelectedCalorieAmount(event: any) {
    this.selectedCalorieAmount = event.target.value;
  }
  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }
  setOpenBudgetSave(isOpen: boolean) {
    //   //saving logic
       if (!isOpen) {
         this.selectedBudgetRange = this.selectedPriceRange;
       }
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
            // Add more protein options as needed
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
            // Add more carbs options as needed
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
            // Add more fats options as needed
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
            this.protein = value['protein'].value;
            this.carbs = value['carbs'].value;
            this.fats = value['fats'].value;
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
    //saving logic
    if (this.protein && this.carbs && this.fats) {
    if (!isOpen) {
      this.MacroRatio = this.protein + ' : ' + this.carbs + ' : ' + this.fats;
    }
    this.isMacroModalOpen = isOpen;
    }
  }
  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
  }
  setOpenAllergiesSave(isOpen: boolean) {
    //saving logic
    this.isAllergiesModalOpen = isOpen;
  }
  setOpenCooking(isOpen: boolean) {
    this.isCookingModalOpen = isOpen;
  }
  setOpenCookingSave(isOpen: boolean) {
    if (!isOpen) {
      // Update the selected value of the radio group
      this.selectedCookingTime = this.cookingTime;
    }
    this.isCookingModalOpen = isOpen;
  }
 

  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {
    //saving logic
    if (this.height && this.weight) {
    if (!isOpen) {
      this.selectedBMICalculator = this.calculateBMI(this.height, this.weight);
    this.isBMIModalOpen = isOpen;
    }
  }
  }
  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }

  setOpenShoppingSave(isOpen: boolean) {
    //saving logic
    if (!isOpen) {
      // Update the selected value of the radio group
      this.selectedShoppingInterval = this.shoppingInterval;
    }
    this.isShoppingModalOpen = isOpen;
  }

  //Defintion of Modal answers 


  //function to calculate and display BMI
  calculateBMI(height: number, weight: number): number {
    const bmi = height / weight;
    return Number(bmi.toFixed(2));
  }
  
  
  
  



}
////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////// Example section 
 


