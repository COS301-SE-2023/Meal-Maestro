import { Component, OnInit, QueryList, ViewChildren, ViewChild } from '@angular/core';
import { IonModal, IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FoodListItemComponent } from '../../components/food-list-item/food-list-item.component';
import { FoodItemI } from '../../models/interfaces.model';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { OverlayEventDetail } from '@ionic/core/components';


@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, FoodListItemComponent],
})
export class PantryPage implements OnInit{
  @ViewChildren(FoodListItemComponent) foodListItem!: QueryList<FoodListItemComponent>;
  @ViewChild(IonModal) modal!: IonModal;

  segment: string = 'pantry';
  pantryItems: FoodItemI[] = [];
  newItem: FoodItemI = {
    name: '',
    quantity: 0,
    weight: 0,
  };

  constructor(public r : Router, private pantryService: PantryApiService) {}

  async ngOnInit() {
    this.pantryService.getPantryItems().subscribe((data) => {
      this.pantryItems = data;
    });
  }

  async addItemToPantry(event : Event){
    var ev = event as CustomEvent<OverlayEventDetail<FoodItemI>>;

    if (ev.detail.role === 'confirm') {
      this.pantryService.addToPantry(ev.detail.data!).subscribe((data) => {
        console.log(data);
        this.pantryItems.push(data);
        this.newItem = {
          name: '',
          quantity: 0,
          weight: 0,
        };
      });
    }
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

  dismissModal(){
    this.modal.dismiss(null, 'cancel');
  }

  confirmModal(){
    this.modal.dismiss(this.newItem, 'confirm');
  }

}
