import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
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

  fIns: string[] = [];
  fIng: string[] = [];

  constructor(private modalController: ModalController, private addService: AddRecipeService) { }

  ngOnInit() {
    if (this.item && this.item.instructions) {
      this.formatIns(this.item.instructions);
    }

    if (this.item && this.item.ingredients) {
      this.formatIng(this.item.ingredients);
    }
  }

  private formatIns(ins: string) {
    const insArr: string[] = ins.split(/\d+\.\s+/);
    this.fIns = insArr.filter(instruction => instruction.trim() !== '');
  }

  private formatIng(ing: string) {
    const ingArr: string[] = ing.split(/,[^()]*?(?![^(]*\))/);
    this.fIng = ingArr.map((ingredient) => ingredient.trim());
  }

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