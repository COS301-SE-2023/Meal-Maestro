import { Component, OnInit, QueryList, ViewChildren, ViewChild } from '@angular/core';
import { IonModal, IonicModule, SearchbarChangeEventDetail } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces';
import { OverlayEventDetail } from '@ionic/core/components';
import { AuthenticationService, ErrorHandlerService, PantryApiService, ShoppingListApiService } from '../../services/services';


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
  searchTerm: string = '';
  newItem: FoodItemI = {
    name: '',
    quantity: null,
    weight: null,
  };

  constructor(public r : Router, 
              private pantryService: PantryApiService, 
              private shoppingListService: ShoppingListApiService,
              private errorHandlerService: ErrorHandlerService,
              private auth: AuthenticationService) {}

  async ngOnInit() {
    this.fetchItems();
  }

  async fetchItems(){
    this.pantryService.getPantryItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body){
            this.pantryItems = response.body;
          }
        }
      },
      error: (err) => {
        if (err.status === 403){
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          )
          this.auth.logout();
        }else{
          this.errorHandlerService.presentErrorToast(
            'Error loading pantry items',
            err
          )
        }
      }
    })

    this.shoppingListService.getShoppingListItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body){
            this.shoppingItems = response.body;
          }
        }
      },
      error: (err) => {
        if (err.status === 403){
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          )
          this.auth.logout();
        }else{
          this.errorHandlerService.presentErrorToast(
            'Error loading shopping list items',
            err
          )
        }
      }
    });
  }

  async addItemToPantry(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;

    if (ev.detail.role === 'confirm') {
      console.log(ev.detail.data);
      this.pantryService.addToPantry(ev.detail.data!).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body){
              this.pantryItems.push(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                weight: null,
              };
            }
          }
        },
        error: (err) => {
          if (err.status === 403){
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            )
            this.auth.logout();
          }else{
            this.errorHandlerService.presentErrorToast(
              'Error adding item to pantry',
              err
            )
          }
        }
      });
    }
  }

  async addItemToShoppingList(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;
    if (ev.detail.role === 'confirm') {
      this.shoppingListService.addToShoppingList(ev.detail.data!).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body){
              this.shoppingItems.push(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                weight: null,
              };
            }
          }
          
        },
        error: (err) => {
          if (err.status === 403){
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            )
            this.auth.logout();
          }else{
            this.errorHandlerService.presentErrorToast(
              'Error adding item to shopping list',
              err
            )
          }
        }
      });
    }
  }

  async onItemDeleted(item : FoodItemI){
    if (this.segment === 'pantry'){
      this.pantryService.deletePantryItem(item).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.pantryItems = this.pantryItems.filter((i) => i.name !== item.name);
          }
        },
        error: (err) => {
          if (err.status === 403){
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            )
            this.auth.logout();
          }else{
            this.errorHandlerService.presentErrorToast(
              'Error deleting item from pantry',
              err
            )
          }
        }
      });
    } else if (this.segment === 'shopping'){
      this.shoppingListService.deleteShoppingListItem(item).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.shoppingItems = this.shoppingItems.filter((i) => i.name !== item.name);
          }
        },
        error: (err) => {
          if (err.status === 403){
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            )
            this.auth.logout();
          }else{
            this.errorHandlerService.presentErrorToast(
              'Error deleting item from shopping list',
              err
            )
          }
        }
      });
    }
  }

 async onItemBought(item : FoodItemI){
  this.shoppingListService.buyItem(item).subscribe({
    next: (response) => {
      if (response.status === 200) {
        if (response.body){
          this.pantryItems = response.body;
          this.shoppingItems = this.shoppingItems.filter((i) => i.name !== item.name);

          this.errorHandlerService.presentSuccessToast("Item Bought!");
        }
      }
    },
    error: (err) => {
      if (err.status === 403){
        this.errorHandlerService.presentErrorToast(
          'Unauthorize access. Please login again.',
          err
        )
        this.auth.logout();
      } else {
        this.errorHandlerService.presentErrorToast(
          'Error buying item.',
          err
        )
      }
    }
  })
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

  doRefresh(event : any){
    setTimeout(() => {
      this.fetchItems();
      event.target.complete();
    }, 2000);
  }

  search(event: any) {
    this.searchTerm = event.detail.value;
  }

  isVisible(itemName: String){ 
    // decides whether to show item based on search term

    if (!this.searchTerm) return true;
    return itemName.toLowerCase().includes(this.searchTerm.toLowerCase());
  }
}
