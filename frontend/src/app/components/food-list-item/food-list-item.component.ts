import { Component, Input, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { FoodItem } from '../../models/fooditem.model';
import { PantryApiService } from '../../services/pantry-api.service';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class FoodListItemComponent  implements OnInit {
  @Input() item! : FoodItem;
  constructor(pantryService : PantryApiService) { }

  ngOnInit() {}

  async deleteItem(){
    console.log("delete item clicked " + this.item.name);
  }

  async editItem(){
    console.log("edit item clicked " + this.item.name);
  }
}
