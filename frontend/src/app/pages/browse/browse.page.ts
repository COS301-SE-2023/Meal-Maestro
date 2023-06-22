import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { Router } from '@angular/router';
import { RecipeComponent } from '../../components/recipe/recipe.component';

@Component({
  selector: 'app-browse',
  templateUrl: './browse.page.html',
  styleUrls: ['./browse.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent, RecipeComponent], 
})
export class BrowsePage {

  myArray: { title: string, description: string, url: string, recipe: string }[] = [
    { title: "Greek Salad with Chicken", description: "A light and refreshing salad featuring a flavorful and protein-packed option for a healthy meal prep.", url: "https://images.unsplash.com/photo-1580013759032-c96505e24c1f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1818&q=80", recipe: "RECIPE OF : Greek Salad with Chicken" },
    { title: "Curry Tofu Stir-Fry", description: "It's a balanced and refreshing option packed with protein and nutrients.", url: "https://images.unsplash.com/photo-1564834724105-918b73d1b9e0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=388&q=80",recipe: "RECIPE OF : Curry Tofu Stir-Fry" },
    { title: "Fried Chicken Tenders", description: "Crispy and golden-brown on the outside, tender and juicy on the inside, the classic comfort food favorite", url: "https://images.unsplash.com/photo-1614398751058-eb2e0bf63e53?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1014&q=80", recipe: "RECIPE OF : Fried Chicken Tenders" },
    { title: "Spaghetti & Prawns", description: "This Italian-inspired recipe is a crowd-pleaser and can be made in advance, making it perfect for meal prepping.", url: "https://images.unsplash.com/photo-1563379926898-05f4575a45d8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80", recipe: "RECIPE OF : Spaghetti & Prawns" },
    // Additional entries...
    
  ];
  

  constructor() { }


}
