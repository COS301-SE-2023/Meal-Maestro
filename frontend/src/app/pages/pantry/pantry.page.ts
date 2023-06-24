import { Component, OnInit, QueryList, ViewChildren, ViewChild } from '@angular/core';
import { IonModal, IonicModule, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';
import { OverlayEventDetail } from '@ionic/core/components';
import { ErrorHandlerService } from '../../services/error-handler/error-handler.service';


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

  constructor(public r : Router, 
              private pantryService: PantryApiService, 
              private shoppingListService: ShoppingListApiService,
              private errorHandlerService: ErrorHandlerService) {}

  async ngOnInit() {
    this.pantryService.getPantryItems().subscribe({
      next: (data) => {
        this.pantryItems = data;
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error loading pantry items',
          err
        )
      }
    })
    this.shoppingListService.getShoppingListItems().subscribe({
      next: (data) => {
        this.shoppingItems = data;
      },
      error: (err) => {
        this.errorHandlerService.presentErrorToast(
          'Error loading shopping list items',
          err
        )
      }
    });
  }

  async addItemToPantry(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;

    if (ev.detail.role === 'confirm') {
      this.pantryService.addToPantry(ev.detail.data!).subscribe({
        next: (data) => {
          console.log(data);
          this.pantryItems.push(data);
          this.newItem = {
            name: '',
            quantity: null,
            weight: null,
          };
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error adding item to pantry',
            err
          )
        }
      });
    }
  }

  async addItemToShoppingList(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;
    if (ev.detail.role === 'confirm') {
      this.shoppingListService.addToShoppingList(ev.detail.data!).subscribe({
        next: (data) => {
          console.log(data);
          this.shoppingItems.push(data);
          this.newItem = {
            name: '',
            quantity: null,
            weight: null,
          };
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error adding item to shopping list',
            err
          )
        }
      });
    }
  }

  onItemDeleted(item : FoodItemI){
    if (this.segment === 'pantry'){
      this.pantryService.deletePantryItem(item).subscribe({
        next: () => {
          this.pantryItems = this.pantryItems.filter((i) => i.name !== item.name);
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error deleting item from pantry',
            err
          )
        }
      });
    } else if (this.segment === 'shopping'){
      this.shoppingListService.deleteShoppingListItem(item).subscribe({
        next: () => {
          this.shoppingItems = this.shoppingItems.filter((i) => i.name !== item.name);
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error deleting item from shopping list',
            err
          )
        }
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
