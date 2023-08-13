import { AfterViewInit, Component, ElementRef, EventEmitter, Input, NgZone, Output, ViewChild } from '@angular/core';
import { ActionSheetController, Animation, AnimationController, IonItemSliding, IonicModule, PickerController } from '@ionic/angular';
import { FoodItemI } from '../../models/interfaces';
import { ErrorHandlerService, PantryApiService, ShoppingListApiService } from '../../services/services';
import { CommonModule } from '@angular/common';
import { parse } from 'path';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class FoodListItemComponent  implements AfterViewInit {
  @Input() item! : FoodItemI;
  @Input() segment! : 'pantry' | 'shopping';
  @Input() isVisible! : boolean;
  @Output() itemDeleted: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @Output() itemBought: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @ViewChild(IonItemSliding, { static: false }) slidingItem!: IonItemSliding;
  @ViewChild(IonItemSliding, { read: ElementRef }) slidingItemRef!: ElementRef<HTMLIonItemSlidingElement>;

  private buyAnimation!: Animation;
  private deleteAnimation!: Animation;
  private boughtItem?: FoodItemI;
  private deletedItem?: FoodItemI;

  constructor(private pantryService : PantryApiService, 
              private actionSheetController: ActionSheetController, 
              private pickerController: PickerController,
              private shoppingListService: ShoppingListApiService,
              private errorHandlerService: ErrorHandlerService,
              private animationCtrl: AnimationController,
              private ngZone: NgZone) { }

  ngAfterViewInit() {
    this.buyAnimation = this.animationCtrl
      .create()
      .addElement(this.slidingItemRef.nativeElement)
      .duration(200)
      .iterations(1)
      .keyframes([
        { offset: 0, transform: 'translateX(0px)' },
        { offset: 0.4, transform: 'translateX(10%)' },
        { offset: 1, transform: 'translateX(-100%)' },
      ])
      .onFinish(() => {
        this.ngZone.run(() => {
          this.itemBought.emit(this.boughtItem);
        });
      });


    this.deleteAnimation = this.animationCtrl
    .create()
    .addElement(this.slidingItemRef.nativeElement)
    .duration(200)
    .iterations(1)
    .keyframes([
      { offset: 0, transform: 'translateX(0px)' },
      { offset: 0.4, transform: 'translateX(-10%)' },
      { offset: 1, transform: 'translateX(100%)' },
    ])
    .onFinish(() => {
      this.ngZone.run(() => {
        this.itemDeleted.emit(this.deletedItem);
      });
    });
  }

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
            unit: this.item.unit,
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
      this.deletedItem = data;
      this.deleteAnimation.play();
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
            unit: this.item.unit,
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
      this.boughtItem = data;
      this.buyAnimation.play();
      // this.itemBought.emit(data);
    }else if(role === 'cancel'){
      this.closeItem();
    }
  }

  async openQuantityPicker(){
    const quantityOptions = [];

    let quantitySelectedIndex = 0;

    let lowerBound = 0;
    let upperBound = 100;
    let decimal = 0;
    let step = 1;

    // Adjust step based on the unit
    if (this.item.unit === 'g' || this.item.unit === 'ml') {
      step = 100;
      upperBound = 3000;
    } else if (this.item.unit === 'kg' || this.item.unit === 'l') {
      step = 0.1;
      upperBound = 20;
      decimal = 1;
    }

    for(let i = lowerBound; i <= upperBound; i += step){
      let value = parseFloat(i.toFixed(decimal));
      quantityOptions.push({
        text: value.toString(),
        value: value,
      });
      
      if(value === this.item.quantity){
        quantitySelectedIndex = i / step;
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

            if(this.item.unit === 'g' || this.item.unit === 'ml'){
              if(value.quantity.value >= 1000){
                this.item.unit = this.item.unit === 'g' ? 'kg' : 'l';
                value.quantity.value /= 1000;
              }
            } else if(this.item.unit === 'kg' || this.item.unit === 'l'){
              if(value.quantity.value < 1){
                this.item.unit = this.item.unit === 'kg' ? 'g' : 'ml';
                value.quantity.value *= 1000;
              }
            }

            const updatedItem: FoodItemI = {
              name: this.item.name,
              quantity: value.quantity.value,
              unit: this.item.unit,
            }
            if(this.segment === 'pantry') {
              this.pantryService.updatePantryItem(updatedItem).subscribe({
                next: (response) => {
                  if (response.status === 200) {
                    this.item.quantity = value.quantity.value;
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
