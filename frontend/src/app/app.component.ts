import { Component, EnvironmentInjector, inject } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
//import { register } from 'swiper/element/bundle';
//register();
 
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class AppComponent {
  public environmentInjector = inject(EnvironmentInjector);

  constructor() {}

  //const url = 'http://localhost:7867/removeFromPantry
  // const body = {
  //   id : 'pantryItemId'
  // }

  // this.http.post(url, body)

}
