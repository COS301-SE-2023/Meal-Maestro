import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { RecipeItemI } from '../../models/recipeItem.model';
import { RecipeBookPage } from '../../pages/recipe-book/recipe-book.page';
import { CommonModule } from '@angular/common';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';
import { MealI } from '../../models/interfaces';

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.scss'],
  standalone: true,
  imports: [IonicModule, RecipeBookPage, CommonModule]
})
export class RecipeDetailsComponent implements OnInit {
  @Input() item!: MealI;
  @Input() items!: MealI[];

  constructor(private modalController: ModalController, private addService: AddRecipeService) { }

  ngOnInit() {}

  closeModal() {
    this.modalController.dismiss();
  }

  notSaved(): boolean {
    return !this.items.includes(this.item);
  } 

  addRecipe(item: MealI) {
    this.addService.setRecipeItem(item);
  }
}