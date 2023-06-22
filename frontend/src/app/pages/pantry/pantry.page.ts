import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItem } from '../../models/fooditem.model';
import { PantryApiService } from '../../services/pantry-api.service';


@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FoodListItemComponent],
})
export class PantryPage implements OnInit{

  segment: string = 'pantry';
  pantryItems: FoodItem[] = [];

  constructor(public r : Router, private pantryService: PantryApiService) {}

  async ngOnInit() {
    this.pantryService.getPantryItems().subscribe((data) => {
      this.pantryItems = data;
      this.pantryItems = [{name: 'food1', quantity: 1, weight: 1}, {name: 'food2', quantity: 2, weight: 2}, {name: 'food3', quantity: 3, weight: 3}];
      console.log(data);
      console.log(this.pantryItems);
    });
  }

  async addItem(){
    console.log("add item clicked");
  }
  
  segmentChanged(event : any){
    this.segment = event.detail.value;
  }
}
