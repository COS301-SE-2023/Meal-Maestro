import { Component, Input } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { MealI } from '../../models/meal.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule]
})
export class RecipeItemComponent {
  // @Input() image!: string;
  // @Input() title!: string;
  @Input() meal!:MealI;
  constructor(private modalController: ModalController) { }

  closeModal() {
    this.modalController.dismiss();
  }
  isModalOpen = false;
  currentObject :any
  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  setCurrent(o : any) {
    this.currentObject = o;
  }
}

