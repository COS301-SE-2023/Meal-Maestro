import { Component, OnInit } from '@angular/core';
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
  item! : FoodItem;
  constructor() { }

  ngOnInit() {}

}
