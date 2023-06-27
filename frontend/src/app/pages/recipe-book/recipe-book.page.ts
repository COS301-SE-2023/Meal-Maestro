import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ModalController } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.page.html',
  styleUrls: ['./recipe-book.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class RecipeBookPage implements OnInit {
  items = [
    { image: 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80', title: 'Salmon' },
    { image: '/assets/img2.jpg', title: 'Stir-fry' },
    { image: '/assets/img4.jpg', title: 'Pancakes' },
    { image: '/assets/img3.jpg', title: 'Raspberry Fruit Salad' }
  ];

  async openModal(item: any) {
    const modal = await this.modalController.create({
      component: RecipeItemComponent,
      componentProps: {
        image: item.image,
        title: item.title
      }
    });
    await modal.present();
  }


  constructor(
    private modalController: ModalController,
    public r: Router,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService
  ) {}

  async ngOnInit() {
    for (let index = 0; index < 4; index++) {
      this.mealGenerationservice.getMeal().subscribe({
        next: (data) => {
          if (Array.isArray(data)) {
            this.meals.push(...data);
          } else {
            this.meals.push(data);
          }

          console.log(this.meals);
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error loading recipe items',
            err
          );
        },
      });
    }

  }

}
