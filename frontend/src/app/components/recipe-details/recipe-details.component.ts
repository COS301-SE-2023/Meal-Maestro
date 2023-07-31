import { Component, Input, OnInit } from '@angular/core';
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
  @Input() image!: string;
  @Input() title!: string;

  constructor(private modalController: ModalController) { }

  ngOnInit() {}

  closeModal() {
    this.modalController.dismiss();
  }

  recipe: RecipeItemI = {
    image: this.image,
    title: this.title,
  };
}