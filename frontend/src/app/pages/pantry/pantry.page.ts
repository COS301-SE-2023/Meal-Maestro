import {
  Component,
  OnInit,
  QueryList,
  ViewChildren,
  ViewChild,
} from '@angular/core';
import {
  AlertController,
  AlertInput,
  IonModal,
  IonicModule,
  ViewWillEnter,
} from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces';
import { OverlayEventDetail } from '@ionic/core/components';
import {
  BarcodeScanner,
  Barcode,
  ScanResult,
} from '@capacitor-mlkit/barcode-scanning';
import {
  AuthenticationService,
  ErrorHandlerService,
  LoginService,
  PantryApiService,
  ShoppingListApiService,
  BarcodeApiService,
} from '../../services/services';

@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, FoodListItemComponent],
})
export class PantryPage implements OnInit, ViewWillEnter {
  @ViewChildren(FoodListItemComponent)
  foodListItem!: QueryList<FoodListItemComponent>;
  @ViewChild(IonModal) modal!: IonModal;

  isBarcodeSupported: boolean = false;
  segment: 'pantry' | 'shopping' | null = 'pantry';
  isLoading: boolean = false;
  pantryItems: FoodItemI[] = [];
  shoppingItems: FoodItemI[] = [];
  searchTerm: string = '';
  currentSort: string = 'name-down';
  newItem: FoodItemI = {
    name: '',
    quantity: null,
    unit: undefined,
    price: undefined,
  };

  constructor(
    public r: Router,
    private pantryService: PantryApiService,
    private shoppingListService: ShoppingListApiService,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService,
    private loginService: LoginService,
    private barcodeApiService: BarcodeApiService,
    private alertController: AlertController
  ) {}

  async ngOnInit() {
    BarcodeScanner.isSupported().then((result) => {
      this.isBarcodeSupported = result.supported;
    });
  }

  async ionViewWillEnter() {
    if (!this.loginService.isPantryRefreshed()) {
      this.fetchItems();
      this.loginService.setPantryRefreshed(true);
    }
  }

  async fetchItems() {
    this.isLoading = true;
    this.pantryService.getPantryItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.pantryItems = response.body;
            console.log(this.pantryItems);
            this.isLoading = false;
            this.sortNameDescending();
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          );
          this.isLoading = false;
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading pantry items',
            err
          );
          this.isLoading = false;
        }
      },
    });

    this.shoppingListService.getShoppingListItems().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.shoppingItems = response.body;
            this.isLoading = false;
            this.sortNameDescending();
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading shopping list items',
            err
          );
        }
      },
    });
  }

  async addItemToPantry(event: Event) {
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;

    if (ev.detail.role === 'confirm') {
      this.pantryService.addToPantry(ev.detail.data!).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body) {
              this.pantryItems.unshift(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                unit: 'pcs',
                price: undefined,
              };
            }
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error adding item to pantry',
              err
            );
          }
        },
      });
    }
  }

  async addItemToShoppingList(event: Event) {
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;
    if (ev.detail.role === 'confirm') {
      this.shoppingListService.addToShoppingList(ev.detail.data!).subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body) {
              this.shoppingItems.unshift(response.body);
              this.newItem = {
                name: '',
                quantity: null,
                unit: 'pcs',
                price: undefined,
              };
            }
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error adding item to shopping list',
              err
            );
          }
        },
      });
    }
  }

  async onItemDeleted(item: FoodItemI) {
    if (this.segment === 'pantry') {
      this.pantryService.deletePantryItem(item).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.pantryItems = this.pantryItems.filter(
              (i) => i.name !== item.name
            );
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error deleting item from pantry',
              err
            );
          }
        },
      });
    } else if (this.segment === 'shopping') {
      this.shoppingListService.deleteShoppingListItem(item).subscribe({
        next: (response) => {
          if (response.status === 200) {
            this.shoppingItems = this.shoppingItems.filter(
              (i) => i.name !== item.name
            );
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error deleting item from shopping list',
              err
            );
          }
        },
      });
    }
  }

  async onItemBought(item: FoodItemI) {
    this.shoppingListService.buyItem(item).subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.pantryItems = response.body;
            this.shoppingItems = this.shoppingItems.filter(
              (i) => i.name !== item.name
            );
            this.errorHandlerService.presentSuccessToast('Item Bought!');
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorized access. Please login again.',
            err
          );
          this.auth.logout();
        } else if (err.status === 409) {
          this.errorHandlerService.presentErrorToast(
            'Cannot convert units',
            err
          );
        } else {
          this.errorHandlerService.presentErrorToast('Error buying item.', err);
        }
      },
    });
  }

  closeSlidingItems() {
    this.foodListItem.forEach((item) => {
      item.closeItem();
    });
  }

  segmentChanged(event: any) {
    if (event.detail.value !== 'pantry' && event.detail.value !== 'shopping') {
      this.segment = 'pantry';
    } else {
      this.segment = event.detail.value;
    }
    this.closeSlidingItems();
  }

  dismissModal() {
    this.modal.dismiss(null, 'cancel');
    this.newItem = {
      name: '',
      quantity: null,
      unit: 'pcs',
      price: undefined,
    };
  }

  confirmModal() {
    if (this.newItem.name === '') {
      this.errorHandlerService.presentErrorToast(
        'Please enter a name for the item',
        'No name entered'
      );
      return;
    }
    if (this.newItem.quantity !== null && this.newItem.quantity < 0) {
      this.errorHandlerService.presentErrorToast(
        'Please enter a valid quantity',
        'Invalid quantity'
      );
      return;
    }
    if (this.newItem.quantity === null) {
      this.errorHandlerService.presentErrorToast(
        'Please enter a quantity',
        'No quantity entered'
      );
      return;
    }
    this.modal.dismiss(this.newItem, 'confirm');
  }

  doRefresh(event: any) {
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

  isVisible(itemName: String) {
    // decides whether to show item based on search term

    if (!this.searchTerm) return true;
    return itemName.toLowerCase().includes(this.searchTerm.toLowerCase());
  }

  changeSort(sort1: string, sort2: string) {
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
    if (this.segment === 'pantry') {
      this.pantryItems.sort((a, b) => {
        return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1;
      });
    } else if (this.segment === 'shopping') {
      this.shoppingItems.sort((a, b) => {
        return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1;
      });
    }
  }

  sortNameAscending(): void {
    if (this.segment === 'pantry') {
      this.pantryItems.sort((a, b) => {
        return a.name.toLowerCase() > b.name.toLowerCase() ? -1 : 1;
      });
    } else if (this.segment === 'shopping') {
      this.shoppingItems.sort((a, b) => {
        return a.name.toLowerCase() > b.name.toLowerCase() ? -1 : 1;
      });
    }
  }

  sortAmountDescending(): void {
    const convertToCommonUnit = (item: FoodItemI) => {
      let quantity = item.quantity || 0;
      if (item.unit === 'kg') {
        quantity *= 1000; // Convert kilograms to grams
      } else if (item.unit === 'l') {
        quantity *= 1000; // Convert liters to milliliters, if needed
      }
      // Add other unit conversions as needed
      return quantity;
    };

    const sortFunction = (a: FoodItemI, b: FoodItemI) => {
      return convertToCommonUnit(a) > convertToCommonUnit(b) ? -1 : 1;
    };

    if (this.segment === 'pantry') {
      this.pantryItems.sort(sortFunction);
    } else if (this.segment === 'shopping') {
      this.shoppingItems.sort(sortFunction);
    }
  }

  sortAmountAscending(): void {
    const convertToCommonUnit = (item: FoodItemI) => {
      let quantity = item.quantity || 0;
      if (item.unit === 'kg') {
        quantity *= 1000; // Convert kilograms to grams
      } else if (item.unit === 'l') {
        quantity *= 1000; // Convert liters to milliliters, if needed
      }
      // Add other unit conversions as needed
      return quantity;
    };

    const sortFunction = (a: FoodItemI, b: FoodItemI) => {
      return convertToCommonUnit(a) < convertToCommonUnit(b) ? -1 : 1;
    };

    if (this.segment === 'pantry') {
      this.pantryItems.sort(sortFunction);
    } else if (this.segment === 'shopping') {
      this.shoppingItems.sort(sortFunction);
    }
  }

  async scan(): Promise<void> {
    const granted = await this.requestPermissions();
    if (!granted) {
      this.errorHandlerService.presentErrorToast(
        'Please grant camera permissions to use this feature',
        'Camera permissions not granted'
      );
      return;
    }

    const result = await BarcodeScanner.scan();

    if (
      result.barcodes.length === 0 ||
      result.barcodes[0].displayValue === '' ||
      result.barcodes[0].displayValue === null ||
      result.barcodes[0].displayValue === undefined
    ) {
      return;
    }
    // let result = {
    //   barcodes: [
    //     {
    //       displayValue: '13761238123', // for testing
    //     },
    //   ],
    // };

    if (this.loginService.isShoppingAt() === '') {
      this.askShoppingLocation(result);
    } else {
      this.sendBarcode(result);
    }
  }

  async requestPermissions(): Promise<boolean> {
    const { camera } = await BarcodeScanner.requestPermissions();
    return camera === 'granted' || camera === 'limited';
  }

  async sendBarcode(result: any): Promise<void> {
    // replace any with ScanResult
    console.log(result.barcodes[0].displayValue);
    let code = result.barcodes[0].displayValue;

    this.barcodeApiService
      .findProduct(code, this.loginService.isShoppingAt())
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            if (response.body) {
              if (response.body.name === '') {
                this.barcodeNotFound();
              } else {
                this.newItem = {
                  name: response.body.name,
                  quantity: response.body.quantity,
                  unit: response.body.unit,
                  price: response.body.price,
                };
                this.modal.present();
              }
            }
          }
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorHandlerService.presentErrorToast(
              'Unauthorized access. Please login again.',
              err
            );
            this.auth.logout();
          } else {
            this.errorHandlerService.presentErrorToast(
              'Error finding product',
              err
            );
          }
        },
      });
  }

  async barcodeNotFound() {
    //IMPLEMENT
    const alert = await this.alertController.create({
      header: 'Barcode not found',
      message: 'Please enter the name of the item',
      inputs: [
        {
          name: 'name',
          type: 'text',
          placeholder: 'Name',
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
        },
        {
          text: 'Confirm',
          handler: (data) => {
            this.newItem = {
              name: data.name,
              quantity: null,
              unit: 'pcs',
              price: undefined,
            };
            this.modal.present();
          },
        },
      ],
    });

    await alert.present();
  }

  async askShoppingLocation(code: any) {
    const alert = await this.alertController.create({
      header: 'Shopping Location',
      message: 'Where are you shopping?',
      inputs: [
        {
          label: 'Woolworths',
          type: 'radio',
          value: 'Woolworths',
        },
        {
          label: 'Checkers',
          type: 'radio',
          value: 'Checkers',
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
        },
        {
          text: 'Confirm',
          handler: (data) => {
            this.loginService.setShoppingAt(data);
            this.sendBarcode(code);
          },
        },
      ],
    });

    await alert.present();
  }
}
