import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RecipeItemComponent } from '../../components/recipe-item/recipe-item.component';
import { RecipeItemI } from '../../models/recipeItem.model';
import { RecipeBookApiService } from '../../services/services';

@Component({
  selector: 'app-recipe-book',
  templateUrl: './recipe-book.page.html',
  styleUrls: ['./recipe-book.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, RecipeItemComponent]
})
export class RecipeBookPage implements OnInit {
  items: RecipeItemI[] = [
    { image: 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80', title: 'Salmon' },
    { image: '/assets/img2.jpg', title: 'Stir-fry' },
    { image: '/assets/img4.jpg', title: 'Pancakes' },
    { image: '/assets/img3.jpg', title: 'Raspberry Fruit Salad' }
  ];

  constructor(private recipeService: RecipeBookApiService) { }

  ngOnInit() {
  }

}
