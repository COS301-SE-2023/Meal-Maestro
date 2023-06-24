import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ActionSheetController, IonItemSliding, IonicModule } from '@ionic/angular';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class FoodListItemComponent  implements OnInit {
  @Input() item! : FoodItemI;
  @Output() itemDeleted: EventEmitter<FoodItemI> = new EventEmitter<FoodItemI>();
  @ViewChild(IonItemSliding, { static: false }) slidingItem!: IonItemSliding;

  constructor(private pantryService : PantryApiService, private actionSheetController: ActionSheetController) { }

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
      this.deleteItem(data);
    }else if(role === 'cancel'){
      this.closeItem();
    }
  }

  async deleteItem(food : FoodItemI){
     this.pantryService.deletePantryItem(food).subscribe(() => {
      this.itemDeleted.emit(food)
     });
  }

  async editItem(){
    console.log("edit item clicked " + this.item.name);
  }

  public async closeItem(){
    this.slidingItem.close();
  }
}
