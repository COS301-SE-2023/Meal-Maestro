import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-daily-meals',
  templateUrl: './daily-meals.component.html',
  styleUrls: ['./daily-meals.component.scss'],
  standalone : true,
  imports: [CommonModule, IonicModule],
})
export class DailyMealsComponent  implements OnInit {

  @Input() mealsData!: { title: string, description: string, url: string }[];
  isModalOpen = false;
  currentObject :any
  setOpen(isOpen: boolean, o :any) {
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  constructor() { }

  ngOnInit() {}

  setCurrent(o : any) {
    this.currentObject = o;
  }

}
