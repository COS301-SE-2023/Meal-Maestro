import { Component, OnInit, QueryList, ViewChildren, ViewChild } from '@angular/core';
import { IonModal, IonicModule } from '@ionic/angular';
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
  isQuantity: boolean = false;
  isLoading: boolean = false;
  pantryItems: FoodItemI[] = [];
  shoppingItems: FoodItemI[] = [];
  searchTerm: string = '';
  currentSort: string = 'name-down';
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
    this.isLoading = true;
    this.pantryService.getPantryItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body){
            this.pantryItems = response.body;
            this.isLoading = false;
            this.sortNameDescending();
          }
        }
      },
      error: (err) => {
        if (err.status === 403){
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          )
          this.isLoading = false;
          this.auth.logout();
        }else{
          this.errorHandlerService.presentErrorToast(
            'Error loading pantry items',
            err
          )
          this.isLoading = false;
        }
      }
    })

    this.shoppingListService.getShoppingListItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body){
            this.shoppingItems = response.body;
            this.isLoading = false;
            this.sortNameDescending();
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
      this.pantryService.addToPantry(ev.detail.data!).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body){
              this.pantryItems.unshift(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                weight: null,
              };
              this.isQuantity = false;
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
              this.shoppingItems.unshift(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                weight: null,
              };
              this.isQuantity = false;
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
          console.log(this.shoppingItems);
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
    this.newItem = {
      name: '',
      quantity: null,
      weight: null,
    };
    this.isQuantity = false;
  }

  confirmModal(){
    if (this.newItem.name === ''){
      this.errorHandlerService.presentErrorToast('Please enter a name for the item', 'No name entered');
      return;
    }
    if ((this.newItem.quantity !== null && this.newItem.quantity < 0) || 
          (this.newItem.weight !== null && this.newItem.weight < 0)){
      this.errorHandlerService.presentErrorToast('Please enter a valid quantity or weight', 'Invalid quantity or weight');
      return;
    }
    if (this.newItem.quantity === null && this.newItem.weight === null){
      this.errorHandlerService.presentErrorToast('Please enter a quantity or weight', 'No quantity or weight entered');
      return;
    }
    this.modal.dismiss(this.newItem, 'confirm');
  }

  doRefresh(event : any){
    this.isLoading = true;
    setTimeout(() => {
      this.fetchItems();
      this.isLoading = false;
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

  changeSort(sort1: string, sort2: string){
    this.currentSort = this.currentSort === sort1 ? sort2 : sort1;
    this.sortChanged();
  }

  sortChanged(): void {
    switch (this.currentSort) {
      case 'name-down':
        // sort by name descending
        this.sortNameDescending();
        break;
      case 'name-up':
        // sort by name ascending
        this.sortNameAscending();
        break;
      case 'amount-down':
        // sort by amount descending
        this.sortAmountDescending();
        break;
      case 'amount-up':
        // sort by amount ascending
        this.sortAmountAscending();
        break;
      default:
        break;
    }
  }

  sortNameDescending(): void {
    if (this.segment === 'pantry'){
      this.pantryItems.sort((a, b) => {
        return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1;
      });
    } else if (this.segment === 'shopping'){
      this.shoppingItems.sort((a, b) => {
        return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1;
      });
    }
  }

  sortNameAscending(): void {
    if (this.segment === 'pantry'){
      this.pantryItems.sort((a, b) => {
        return a.name.toLowerCase() > b.name.toLowerCase() ? -1 : 1;
      });
    } else if (this.segment === 'shopping'){
      this.shoppingItems.sort((a, b) => {
        return a.name.toLowerCase() > b.name.toLowerCase() ? -1 : 1;
      });
    }
  }

  sortAmountDescending(): void {
    if (this.segment === 'pantry'){
      this.pantryItems.sort((a, b) => {
        return (a.quantity! + a.weight!) > (b.quantity! + b.weight!) ? -1 : 1;
      });
    } else if (this.segment === 'shopping'){
      this.shoppingItems.sort((a, b) => {
        return (a.quantity! + a.weight!) > (b.quantity! + b.weight!) ? -1 : 1;
      });
    }
  }

  sortAmountAscending(): void {
    if (this.segment === 'pantry'){
      this.pantryItems.sort((a, b) => {
        return (a.quantity! + a.weight!) < (b.quantity! + b.weight!) ? -1 : 1;
      });
    } else if (this.segment === 'shopping'){
      this.shoppingItems.sort((a, b) => {
        return (a.quantity! + a.weight!) < (b.quantity! + b.weight!) ? -1 : 1;
      });
    }
  }

}
