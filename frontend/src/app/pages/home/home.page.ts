import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { DailyMealsComponent } from '../../components/daily-meals/daily-meals.component';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule, DailyMealsComponent],
})
export class HomePage {

  mealsArray: { title: string, description: string, url: string, recipe: string }[] = [
    { title: "Greek Salad with Chicken", description: "A light and refreshing salad featuring a flavorful and protein-packed option for a healthy meal prep.", url: "https://images.unsplash.com/photo-1580013759032-c96505e24c1f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1818&q=80", recipe: "RECIPE OF : Greek Salad with Chicken" },
    { title: "Curry Tofu Stir-Fry", description: "It's a balanced and refreshing option packed with protein and nutrients.", url: "https://images.unsplash.com/photo-1564834724105-918b73d1b9e0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=388&q=80",recipe: "RECIPE OF : Curry Tofu Stir-Fry" },
    { title: "Fried Chicken Tenders", description: "Crispy and golden-brown on the outside, tender and juicy on the inside, the classic comfort food favorite", url: "https://images.unsplash.com/photo-1614398751058-eb2e0bf63e53?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1014&q=80", recipe: "RECIPE OF : Fried Chicken Tenders" },
    { title: "Spaghetti & Prawns", description: "This Italian-inspired recipe is a crowd-pleaser and can be made in advance, making it perfect for meal prepping.", url: "https://images.unsplash.com/photo-1563379926898-05f4575a45d8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80", recipe: "RECIPE OF : Spaghetti & Prawns" },
    // Additional entries...
    
  ];
  constructor() {}
}
