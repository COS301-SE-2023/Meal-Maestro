import { Component, Input, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { FoodItem } from '../../models/fooditem.model';

@Component({
  selector: 'app-food-list-item',
  templateUrl: './food-list-item.component.html',
  styleUrls: ['./food-list-item.component.scss'],
  standalone: true,
  imports: [IonicModule],
})
export class FoodListItemComponent  implements OnInit {
  @Input() item! : FoodItem;
  constructor() { }

  ngOnInit() {}

  async deleteItem(){
    console.log("delete item clicked " + this.item.name);
  }

  async editItem(){
    console.log("edit item clicked " + this.item.name);
  }
}
