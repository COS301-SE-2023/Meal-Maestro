import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { Router } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser'

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, ExploreContainerComponent]
})
export class RecipeComponent {

  @Input() arrayData!: { title: string, description: string, url: string }[];
  constructor() { 

  }

  ngOnInit() {}

}
