import { Component, OnInit } from '@angular/core';
import { IonicModule, PickerController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { UserPreferencesI } from '../../models/userpreference.model';

import { CommonModule } from '@angular/common';
import { RangeCustomEvent, RangeValue } from '@ionic/core';
import { Router } from '@angular/router';
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
  constructor( private router: Router , private pickerController: PickerController, private auth: AuthenticationService ) {
    this.selectedPriceRange = '';
    this.user = {
      username: '',
      email: '',
      password: ''
    };
  }

  ngOnInit() {
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


  user : UserI;

  userpreferences : UserPreferencesI = {
    goal: '',
    shopping_interval: '',
    food_preferences: [],
    calorie_amount: 0,
    budget_range: '',
    macro_ratio: {protein: 0, carbs: 0, fats: 0},
    allergies: [],
    cooking_time: 0,
    user_height: 0,
    user_weight: 0,
    user_BMI: 0,

    BMI_set : false,
    cookingtime_set : false,
    allergies_set : false,
    macro_set : false,
    budget_set : false,
    calorie_set : false,
    foodpreferance_set : false,
    shoppinginterfval_set : false,
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

  //
  shoppingintervalToggle: boolean = false;
  preferenceToggle: boolean = false;
  calorieToggle: boolean = false;
  budgetToggle: boolean = false;
  macroToggle: boolean = false;
  allergiesToggle: boolean = false;
  cookingToggle: boolean = false;
  BMIToggle: boolean = false;
 
  



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
  if (this.userpreferences.foodpreferance_set === true) {
    if (this.preferences.vegetarian || this.preferences.vegan || this.preferences.glutenIntolerant || this.preferences.lactoseIntolerant) {
      if (!isOpen) {
        this.displayPreferences = this.getSelectedPreferences();
      }
      this.isPreferencesModalOpen = isOpen;
    }
  }
  else if (this.userpreferences.foodpreferance_set === false) {
    this.userpreferences.food_preferences = [];
    this.displayPreferences = '';
    this.isPreferencesModalOpen = isOpen;
  }
  }
  pereference_Toggle()
{
  this.userpreferences.foodpreferance_set = !this.userpreferences.foodpreferance_set;
}
  
  getSelectedPreferences(): string {
    const selectedPreferences = [];
  
    if (this.preferences.vegetarian) {
      selectedPreferences.push('Vegetarian');
      this.userpreferences.food_preferences.push('Vegetarian');
    }
    if (this.preferences.vegan) {
      selectedPreferences.push('Vegan');
      this.userpreferences.food_preferences.push('Vegan');
    }
    if (this.preferences.glutenIntolerant) {
      selectedPreferences.push('Gluten-intolerant');
      this.userpreferences.food_preferences.push('Gluten-intolerant');
    }
    if (this.preferences.lactoseIntolerant) {
      selectedPreferences.push('Lactose-intolerant');
      this.userpreferences.food_preferences.push('Lactose-intolerant');
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
    if (this.userpreferences.calorie_set === true) {
    if (this.userpreferences.calorie_amount) {
    if (!isOpen) {
    }
    this.isCalorieModalOpen = isOpen;
  }
}
else if (this.userpreferences.calorie_set === false) {
  this.userpreferences.calorie_amount = 0;
  this.isCalorieModalOpen = isOpen;
}
}

  calorieAmount_Toggle(){
    this.userpreferences.calorie_set = !this.userpreferences.calorie_set;
  }

  showSelectedCalorieAmount(event: any) {
    this.userpreferences.calorie_amount = event.target.value;
  }

  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }
  setOpenBudgetSave(isOpen: boolean) {
    if (this.userpreferences.budget_set === true) {
    this.userpreferences.budget_range = this.selectedPriceRange;
      this.isBudgetModalOpen = isOpen;
    }
    else if (this.userpreferences.budget_set === false) {
      this.userpreferences.budget_range = '';
      this.isBudgetModalOpen = isOpen;
    }
  }
  budgetRange_Toggle()
  {
    this.userpreferences.budget_set = !this.userpreferences.budget_set;
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
            this.userpreferences.macro_ratio.protein = value['protein'].value;
            this.userpreferences.macro_ratio.carbs = value['carbs'].value;
            this.userpreferences.macro_ratio.fats = value['fats'].value;
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
    if (this.userpreferences.macro_set === true) {
    if (!isOpen) {
      this.displaying_Macroratio = this.userpreferences.macro_ratio.protein + ' : ' + this.userpreferences.macro_ratio.carbs + ' : ' + this.userpreferences.macro_ratio.fats;
    }
    this.isMacroModalOpen = isOpen;
  }
  else if (this.userpreferences.macro_set === false) {
    this.userpreferences.macro_ratio.protein = 0;
    this.userpreferences.macro_ratio.carbs = 0;
    this.userpreferences.macro_ratio.fats = 0;
    this.displaying_Macroratio = '';
    this.isMacroModalOpen = isOpen;
  }
  }
  macro_Toggle()
  {
    this.userpreferences.macro_set = !this.userpreferences.macro_set;
  }
  setOpenAllergies(isOpen: boolean) {
    this.isAllergiesModalOpen = isOpen;
  }
  setOpenAllergiesSave(isOpen: boolean) {
    if (this.userpreferences.allergies_set === true) {
    if (this.allergens.seafood || this.allergens.nuts || this.allergens.eggs || this.allergens.soy) {
      
        if (!isOpen) {
          this.displayAllergies = this.getSelectedAllergens();
        }
    this.isAllergiesModalOpen = isOpen;
      }
    }
    else if (this.userpreferences.allergies_set === false) {
      this.userpreferences.allergies = [];
      this.displayAllergies = '';
      this.isAllergiesModalOpen = isOpen;
    }
    }
    allergies_Toggle()
    {
      this.userpreferences.allergies_set = !this.userpreferences.allergies_set;
    }

      getSelectedAllergens(): string {
        const selectedAllergens = [];
      
        if (this.allergens.seafood) {
          selectedAllergens.push('Seafood');
          this.userpreferences.allergies.push('Seafood')
        }
        if (this.allergens.nuts) {
          selectedAllergens.push('Nuts');
          this.userpreferences.allergies.push('Nuts')
        }
        if (this.allergens.eggs) {
          selectedAllergens.push('Eggs');
          this.userpreferences.allergies.push('Eggs')
        }
        if (this.allergens.soy) {
          selectedAllergens.push('Soy');
          this.userpreferences.allergies.push('Soy')
        }
      
        if (selectedAllergens.length === 1) {
          return selectedAllergens[0];
        } else if (selectedAllergens.length > 1) {
          return 'Multiple';
        } else {
          return '';
         
        }
        
      }
  setOpenCooking(isOpen: boolean) {
    this.isCookingModalOpen = isOpen;
  }
  setOpenCookingSave(isOpen: boolean) {
    if (this.userpreferences.cookingtime_set === true) {
    this.isCookingModalOpen = isOpen;
    }
    else if (this.userpreferences.cookingtime_set === false){
      this.userpreferences.cooking_time = 0;

      this.isCookingModalOpen = isOpen;
    }
  }
  cookingtime_Toggle()
  {
    this.userpreferences.cookingtime_set = !this.userpreferences.cookingtime_set;
  }

  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {

    if (this.userpreferences.BMI_set === true) {
    if (!isOpen) {
      //call BMI calculation fuction
    this.isBMIModalOpen = isOpen;
    }
  }
  else if (this.userpreferences.BMI_set === false) {
    this.userpreferences.user_BMI = 0;

    this.isBMIModalOpen = isOpen;
  }
}
  BMI_Toggle()
  {
    this.userpreferences.BMI_set = !this.userpreferences.BMI_set;
  }
  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }

  setOpenShoppingSave(isOpen: boolean) {
    if (this.userpreferences.shoppinginterfval_set === true ) {
    if (this.shopping_interval === 'other') {
        this.userpreferences.shopping_interval = this.shoppingIntervalOtherValue.toString();
    }
      else if (this.shopping_interval == 'weekly' || this.shopping_interval == 'biweekly' || this.shopping_interval == 'monthly')
      {
        this.userpreferences.shopping_interval = this.shopping_interval;
      }
    this.isShoppingModalOpen = isOpen;
  }
  else if (this.userpreferences.shoppinginterfval_set === false){
    this.userpreferences.shopping_interval = '';
    this.isShoppingModalOpen = isOpen;
  }
}

shoppingInterval_Toggle()
{
  this.userpreferences.shoppinginterfval_set = !this.userpreferences.shoppinginterfval_set;
}
}



 


