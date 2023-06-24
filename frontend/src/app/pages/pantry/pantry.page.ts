import { Component, OnInit, QueryList, ViewChildren, ViewChild } from '@angular/core';
import { IonModal, IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { OverlayEventDetail } from '@ionic/core/components';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';


@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, FoodListItemComponent],
})
export class PantryPage implements OnInit{
  @ViewChildren(FoodListItemComponent) foodListItem!: QueryList<FoodListItemComponent>;
  @ViewChild(IonModal) modal!: IonModal;

  segment: 'pantry'|'shopping'| null = 'pantry';
  pantryItems: FoodItemI[] = [];
  shoppingItems: FoodItemI[] = [];
  newItem: FoodItemI = {
    name: '',
    quantity: null,
    weight: null,
  };

  constructor(public r : Router, private pantryService: PantryApiService, private shoppingListService: ShoppingListApiService) {}

  async ngOnInit() {
    this.pantryService.getPantryItems().subscribe((data) => {
      this.pantryItems = data;
    });
    this.shoppingListService.getShoppingListItems().subscribe((data) => {
      this.shoppingItems = data;
    });
  }

  async addItemToPantry(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;

    if (ev.detail.role === 'confirm') {
      this.pantryService.addToPantry(ev.detail.data!).subscribe((data) => {
        console.log(data);
        this.pantryItems.push(data);
        this.newItem = {
          name: '',
          quantity: null,
          weight: null,
        };
      });
    }
  }

  async addItemToShoppingList(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;
    if (ev.detail.role === 'confirm') {
      this.shoppingListService.addToShoppingList(ev.detail.data!).subscribe((data) => {
        console.log(data);
        this.shoppingItems.push(data);
        this.newItem = {
          name: '',
          quantity: null,
          weight: null,
        };
      });
    }
  }

  onItemDeleted(item : FoodItemI){
    if (this.segment === 'pantry'){
      this.pantryService.deletePantryItem(item).subscribe(() => {
        this.pantryItems = this.pantryItems.filter((i) => i.name !== item.name);
      });
    } else if (this.segment === 'shopping'){
      this.shoppingListService.deleteShoppingListItem(item).subscribe(() => {
        this.shoppingItems = this.shoppingItems.filter((i) => i.name !== item.name);
      });
    }
  }

  closeSlidingItems(){
    this.foodListItem.forEach((item) => {
      item.closeItem();
    });
  }

  segmentChanged(event : any){
    if (event.detail.value !== 'pantry' && event.detail.value !== 'shopping'){
      this.segment = 'pantry';
    }else{
      this.segment = event.detail.value;
    }
    this.closeSlidingItems();
  }

  dismissModal(){
    this.modal.dismiss(null, 'cancel');
  }

  confirmModal(){
    this.modal.dismiss(this.newItem, 'confirm');
  }

}
