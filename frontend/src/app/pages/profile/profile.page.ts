import { Component, ViewChild } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { RangeCustomEvent, RangeValue } from '@ionic/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent, FormsModule, CommonModule],
})
export class ProfilePage {
height: any;
weight: any;
 

  constructor( private router: Router , ) {this.selectedPriceRange = '';
  this.customAmount = 0;
}

selectedShoppingInterval: any;
shoppingInterval: any;
customInterval: any;
protein: any;
carbs: any;
fats: any;
  selectedPriceRange: string;
  customAmount: number;
  lastEmittedValue: RangeValue | undefined; 
  anyChanges: boolean = false;
  message: string | undefined;

  

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

  isPreferancesModalOpen: boolean = false;
  isCalorieModalOpen: boolean = false;
  isBudgetModalOpen: boolean = false;
  isMacroModalOpen: boolean = false;
  isAllergiesModalOpen: boolean = false;
  isCookingModalOpen: boolean = false;
  isBMIModalOpen: boolean = false;
  isShoppingModalOpen: boolean = false;

  setOpenPreferances(isOpen: boolean) {
    this.isPreferancesModalOpen = isOpen;
  }
  setOpenPreferancesSave(isOpen: boolean) {
    //saving logic
    this.isPreferancesModalOpen = isOpen;
  }
  setOpenCalorie(isOpen: boolean) {
    this.isCalorieModalOpen = isOpen;
  }
  setOpenCalorieSave(isOpen: boolean) {
    //saving logic
    this.isCalorieModalOpen = isOpen;
  }
  setOpenBudget(isOpen: boolean) {
    this.isBudgetModalOpen = isOpen;
  }
  setOpenBudgetSave(isOpen: boolean) {
    //saving logic
    this.isBudgetModalOpen = isOpen;
  }
  setOpenMacro(isOpen: boolean) {
    this.isMacroModalOpen = isOpen;
  }
  setOpenMacroSave(isOpen: boolean) {
    //saving logic
    this.isMacroModalOpen = isOpen;
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
    //saving logic
    this.isCookingModalOpen = isOpen;
  }
  setOpenBMI(isOpen: boolean) {
    this.isBMIModalOpen = isOpen;
  }
  setOpenBMISave(isOpen: boolean) {
    //saving logic
    this.isBMIModalOpen = isOpen;
  }
  setOpenShopping(isOpen: boolean) {
    this.isShoppingModalOpen = isOpen;
  }
  setOpenShoppingSave(isOpen: boolean) {
    //saving logic
    this.isShoppingModalOpen = isOpen;
  }


  //function to calculate and display BMI
  calculateBMI(){
    let height = this.height;
    let weight = this.weight;
    let bmi = (weight / (height * height)) * 703;
  }



}
////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////// Example section 
 


