import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ActionSheetController, IonItemSliding, IonicModule, PickerController } from '@ionic/angular';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class FoodListItemComponent  implements OnInit {
  @Input() item! : FoodItemI;
  @Input() segment! : 'pantry' | 'shopping';
  @Output() itemDeleted: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @ViewChild(IonItemSliding, { static: false }) slidingItem!: IonItemSliding;

  constructor(private pantryService : PantryApiService, 
              private actionSheetController: ActionSheetController, 
              private pickerController: PickerController,
              private shoppingListService: ShoppingListApiService) { }

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

  async openEditPicker(){
    const quantityOptions = [];
    const weightOptions = [];

    let quantitySelectedIndex = 0;
    let weightSelectedIndex = 0;

    for(let i = 1; i <= 100; i++){
        quantityOptions.push({
            text: String(i),
            value: i
        });

        weightOptions.push({
            text: String(i),
            value: i
        });

        if(i === this.item.quantity) {
          quantitySelectedIndex = i - 1;
        }

        if(i === this.item.weight) {
            weightSelectedIndex = i - 1;
        }
    }

    const picker = await this.pickerController.create({
      columns: [
        {
          name: 'quantity',
          options: quantityOptions,
          selectedIndex: quantitySelectedIndex,
        },
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
              quantity: value.quantity.value,
              weight: value.weight.value,
            }
            if(this.segment === 'pantry') {
              this.pantryService.updatePantryItem(updatedItem).subscribe(() => {
                this.item.quantity = value.quantity.value;
                this.item.weight = value.weight.value;
                this.closeItem();
              });
            } else if (this.segment === 'shopping') {
              this.shoppingListService.updateShoppingListItem(updatedItem).subscribe(() => {
                this.item.quantity = value.quantity.value;
                this.item.weight = value.weight.value;
                this.closeItem();
              });
            }
          },
        },
      ],
      backdropDismiss: true,
    });
    await picker.present();
  }

  public async closeItem(){
    this.slidingItem.close();
  }
}
