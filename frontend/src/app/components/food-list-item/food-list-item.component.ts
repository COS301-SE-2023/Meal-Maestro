import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ActionSheetController, IonItemSliding, IonicModule, PickerController } from '@ionic/angular';
import { FoodItemI } from '../../models/interfaces';
import { ErrorHandlerService, PantryApiService, ShoppingListApiService } from '../../services/services';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class FoodListItemComponent  implements OnInit {
  @Input() item! : FoodItemI;
  @Input() segment! : 'pantry' | 'shopping';
  @Input() isVisible! : boolean;
  @Output() itemDeleted: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @Output() itemBought: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @ViewChild(IonItemSliding, { static: false }) slidingItem!: IonItemSliding;

  constructor(private pantryService : PantryApiService, 
              private actionSheetController: ActionSheetController, 
              private pickerController: PickerController,
              private shoppingListService: ShoppingListApiService,
              private errorHandlerService: ErrorHandlerService) { }

  ngOnInit() {}

  async openDeleteSheet() {
    const actionSheet = await this.actionSheetController.create({
      header: 'Are you sure?',
      buttons: [
        {
          text: 'Delete',
          role: 'destructive',
          data: {
            name: this.item.name,
            quantity: this.item.quantity,
            weight: this.item.weight,
          },
        },
        {
          text: 'Cancel',
          role: 'cancel',
          data: {
            action: 'cancel',
          },
        },
      ],
    });
    await actionSheet.present();

    const { data, role } = await actionSheet.onDidDismiss();
    if (role === 'destructive') {
      this.closeItem();
      this.itemDeleted.emit(data)
    }else if(role === 'cancel'){
      this.closeItem();
    }
  }

  async openAddToPantrySheet(){
    if(this.segment === 'pantry'){
      return;
    }

    const actionSheet = await this.actionSheetController.create({
      header: 'Add to Pantry?',
      buttons: [
        {
          text: 'Add',
          role: 'destructive',
          data: {
            name: this.item.name,
            quantity: this.item.quantity,
            weight: this.item.weight,
          },
        },
        {
          text: 'Cancel',
          role: 'cancel',
          data: {
            action: 'cancel',
          },
        },
      ]
    });
    await actionSheet.present();

    const { data, role } = await actionSheet.onDidDismiss();
    if (role === 'destructive') {
      this.closeItem();
      this.itemBought.emit(data)
      console.log('Bought');
    }else if(role === 'cancel'){
      this.closeItem();
    }
  }

  async choosePicker(){
    if (this.item.quantity !== 0 && this.item.quantity !== null){
      this.openQuantityPicker();
    }
    else if (this.item.weight !== 0 && this.item.weight !== null){
      this.openWeightPicker();
    }
  }

  async openQuantityPicker(){
    const quantityOptions = [];

    let quantitySelectedIndex = 0;

    for(let i = 1; i <= 100; i++){
        quantityOptions.push({
            text: String(i),
            value: i
        });

        if(i === this.item.quantity) {
          quantitySelectedIndex = i - 1;
        }
    }

    const picker = await this.pickerController.create({
      columns: [
        {
          name: 'quantity',
          options: quantityOptions,
          selectedIndex: quantitySelectedIndex,
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            this.closeItem();
          }
        },
        {
          text: 'Confirm',
          handler: (value) => {
            const updatedItem: FoodItemI = {
              name: this.item.name,
              quantity: value.quantity.value,
              weight: 0,
            }
            if(this.segment === 'pantry') {
              this.pantryService.updatePantryItem(updatedItem).subscribe({
                next: (response) => {
                  if (response.status === 200) {
                    this.item.quantity = value.quantity.value;
                    this.item.weight = 0;
                    this.closeItem();
                  }
                },
                error: (err) => {
                  if (err.status === 403){
                    this.errorHandlerService.presentErrorToast('Unauthorized access. Please login again.', err);
                  } else {
                    this.errorHandlerService.presentErrorToast('Error updating item', err);
                  }
                }
              });
            } else if (this.segment === 'shopping') {
              this.shoppingListService.updateShoppingListItem(updatedItem).subscribe({
                next: (response) => {
                  if (response.status === 200) {
                    this.item.quantity = value.quantity.value;
                    this.item.weight = value.weight.value;
                    this.closeItem();
                  }
                },
                error: (err) => {
                  if (err.status === 403){
                    this.errorHandlerService.presentErrorToast('Unauthorized access. Please login again.', err);
                  } else {
                    this.errorHandlerService.presentErrorToast('Error updating item', err);
                  }
                }
              });
            }
          },
        },
      ],
      backdropDismiss: true,
    });
    await picker.present();
  }

  async openWeightPicker(){
    const weightOptions = [];

    let weightSelectedIndex = 0;

    for(let i = 1; i <= 200; i++){
        weightOptions.push({
            text: String(i*10)+'g',
            value: i*10
        });

        if(i === this.item.weight) {
          weightSelectedIndex = i - 1;
        }
    }
    const picker = await this.pickerController.create({
      columns: [
        {
          name: 'weight',
          options: weightOptions,
          selectedIndex: weightSelectedIndex,
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            this.closeItem();
          }
        },
        {
          text: 'Confirm',
          handler: (value) => {
            const updatedItem: FoodItemI = {
              name: this.item.name,
              quantity: 0,
              weight: value.weight.value,
            }
            if(this.segment === 'pantry') {
              this.pantryService.updatePantryItem(updatedItem).subscribe({
                next: (response) => {
                  if (response.status === 200) {
                    this.item.quantity = 0;
                    this.item.weight = value.weight.value;
                    this.closeItem();
                  }
                },
                error: (err) => {
                  if (err.status === 403){
                    this.errorHandlerService.presentErrorToast('Unauthorized access. Please login again.', err);
                  } else {
                    this.errorHandlerService.presentErrorToast('Error updating item', err);
                  }
                }
              });
            } else if (this.segment === 'shopping') {
              this.shoppingListService.updateShoppingListItem(updatedItem).subscribe({
                next: (response) => {
                  if (response.status === 200) {
                    this.item.quantity = value.quantity.value;
                    this.item.weight = value.weight.value;
                    this.closeItem();
                  }
                },
                error: (err) => {
                  if (err.status === 403){
                    this.errorHandlerService.presentErrorToast('Unauthorized access. Please login again.', err);
                  } else {
                    this.errorHandlerService.presentErrorToast('Error updating item', err);
                  }
                }
              });
            }
          },
        },
      ],
      backdropDismiss: true,
    });
    await picker.present();
  }

  public closeItem(){
    if (this.slidingItem) {
      this.slidingItem.close();
    }
  }
}
