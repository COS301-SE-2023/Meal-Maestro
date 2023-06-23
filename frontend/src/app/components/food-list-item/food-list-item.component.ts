import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { IonItemSliding, IonicModule } from '@ionic/angular';
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
  @ViewChild(IonItemSliding, { static: false }) slidingItem!: IonItemSliding;
  constructor(pantryService : PantryApiService) { }

  ngOnInit() {}

  async deleteItem(){
    console.log("delete item clicked " + this.item.name);
  }

  async editItem(){
    console.log("edit item clicked " + this.item.name);
  }

  public async closeItem(){
    this.slidingItem.close();
  }
}
