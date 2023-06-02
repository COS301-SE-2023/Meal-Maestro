import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { RangeCustomEvent, RangeValue } from '@ionic/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile',
  templateUrl: 'profile.page.html',
  styleUrls: ['profile.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent, FormsModule, CommonModule],
})
export class ProfilePage {
  constructor( private router: Router ) {}

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
  
  navToProfile(){
    this.router.navigate(['acc-profile']);
  }
}
