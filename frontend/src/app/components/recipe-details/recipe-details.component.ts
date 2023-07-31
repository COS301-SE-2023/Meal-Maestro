import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { RecipeItemI } from '../../models/recipeItem.model';
import { RecipeBookPage } from '../../pages/recipe-book/recipe-book.page';

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.scss'],
  standalone: true,
  imports: [IonicModule, RecipeBookPage]
})
export class RecipeDetailsComponent implements OnInit {
  @Input() item!: RecipeItemI;
  @Input() items!: RecipeItemI[];
  @Output() addToRecipeBook: EventEmitter<RecipeItemI> = new EventEmitter();

  constructor(private modalController: ModalController) { }

  ngOnInit() {}

  closeModal() {
    this.modalController.dismiss();
  }

  notSaved(): boolean {
    console.log('called :)');
    return !this.items.includes(this.item);
  } 

  addRecipe(item: RecipeItemI) {
    this.addToRecipeBook.emit(item);
  }
}