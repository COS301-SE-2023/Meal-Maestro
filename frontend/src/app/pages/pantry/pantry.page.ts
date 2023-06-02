import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ExploreContainerComponent } from '../../components/explore-container/explore-container.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pantry',
  templateUrl: 'pantry.page.html',
  styleUrls: ['pantry.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent]
})
export class PantryPage {

  constructor(public r : Router) {}
  

  LoadShoppingPage()
  {
    this.r.navigate(['/shopping']);
  }

  // isCardVisible: boolean = false;
  // toggleCardVisibility() {
  //   this.isCardVisible = !this.isCardVisible;
  // }

  // lastEmittedValue: RangeValue | undefined;
  // onIonChange(ev: Event) {
  //   this.lastEmittedValue = (ev as RangeCustomEvent).detail.value;
  // }

}
