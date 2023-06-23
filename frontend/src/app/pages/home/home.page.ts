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
  constructor() {}
}
