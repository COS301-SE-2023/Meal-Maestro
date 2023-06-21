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
      console.log(data);
      console.log(this.pantryItems);
    });
  }
  
  segmentChanged(event : any){
    this.segment = event.detail.value;
    console.log(this.segment);
  }

}
