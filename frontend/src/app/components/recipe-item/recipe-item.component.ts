import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { RecipeDetailsComponent } from '../recipe-details/recipe-details.component';

import { MealI } from '../../models/meal.model';
import { CommonModule } from '@angular/common';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';


@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.scss'],
  standalone: true,

  imports: [IonicModule, CommonModule]

})
export class RecipeItemComponent {
  @Input() items: MealI[] = [];
  @Input() item!: MealI;
  fIns: string[] = [];
  fIng: string[] = [];

  @ViewChild('modalContent', {static: true}) modalContent!: TemplateRef<any>;

  async openModal() {
    const modal = await this.modalController.create({
      component: this.modalContent,
      componentProps: {
        item: this.item,
        items: this.items
      }
    });
    await modal.present();
  }

  public passItems(items: MealI[]): void {
    this.items = items;
  }

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
