import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';


@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FoodListItemComponent],
})
export class PantryPage implements OnInit{
  @ViewChildren(FoodListItemComponent) foodListItem!: QueryList<FoodListItemComponent>;

  segment: string = 'pantry';
  pantryItems: FoodItemI[] = [];

  constructor(public r : Router, private pantryService: PantryApiService) {}

  async ngOnInit() {
    this.pantryService.getPantryItems().subscribe((data) => {
      this.pantryItems = data;
    });
  }

  async addItemToPantry(){
    console.log("add item clicked");
  }

  closeSlidingItems(){
    this.foodListItem.forEach((item) => {
      item.closeItem();
    });
  }

  segmentChanged(event : any){
    this.segment = event.detail.value;
    this.closeSlidingItems();
  }
}
