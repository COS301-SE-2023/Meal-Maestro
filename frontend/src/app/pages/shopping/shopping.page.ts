import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';


@Component({
  selector: 'app-shopping',
  templateUrl: './shopping.page.html',
  styleUrls: ['./shopping.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class ShoppingPage implements OnInit {

  constructor(public r : Router) { }

  LoadPantryPage()
  {
    this.r.navigate(['app/tabs/pantry']);
  }

  ngOnInit() {
  }

}
