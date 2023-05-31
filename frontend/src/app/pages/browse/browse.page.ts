import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-browse',
  templateUrl: './browse.page.html',
  styleUrls: ['./browse.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent]
})
export class BrowsePage {

  constructor() { }


}
