import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { RangeCustomEvent, RangeValue } from '@ionic/core';
import { RouterModule } from '@angular/router';




@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent,FormsModule,CommonModule,RouterModule],
})
export class ProfilePage {
  constructor() {}

  lastEmittedValue: RangeValue | undefined;
  isCardVisible: boolean = false;
  anyChanges: boolean = false;


  onIonChange(ev: Event) {
    this.lastEmittedValue = (ev as RangeCustomEvent).detail.value;
  }


  toggleCardVisibility() {
    this.isCardVisible = !this.isCardVisible;
  }

  showSaveChanges(){
    this.anyChanges = true;
  }
  
}
