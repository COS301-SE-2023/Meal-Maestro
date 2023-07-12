import { Component, Input } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';
import { RecipeDetailsComponent } from '../recipe-details/recipe-details.component';

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.scss'],
  standalone: true,
  imports: [IonicModule]
})
export class RecipeItemComponent {
  @Input() image!: string;
  @Input() title!: string;

  async openModal(item: any) {
    const modal = await this.modalController.create({
      component: RecipeDetailsComponent,
      componentProps: {
        image: item.image,
        title: item.title
      }
    });
    await modal.present();
  }

  constructor(private modalController: ModalController) { }
}
