import { Component, Input, OnInit } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { RecipeItemI } from '../../models/recipeItem.model';

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.scss'],
  standalone: true,
  imports: [IonicModule]
})
export class RecipeDetailsComponent  implements OnInit {
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

  saveToRP(): void {
    this.addToRecipeBook(this.recipe)
      .then((response: Response) => {
        if (response.ok) {
          console.log('Recipe added successfully');
        } else {
          console.log('Failed to add recipe');
        }
      })
      .catch((error: any) => {
        console.error(error);
      });
  }

  addToRecipeBook(recipe: RecipeItemI): Promise<Response> {
    return fetch('../../../backend/src/main/java/fellowship/mealmaestro/services/RecipeBookController/addToRecipeBook', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(recipe),
    });
  }
}
