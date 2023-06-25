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

  @Input() todayData!: {identifier: string,  title: string, description: string, url: string, ingredients:string, instructions:string, cookingTime:string }[];
  @Input() tomorrowData!: {identifier: string,  title: string, description: string, url: string,ingredients:string, instructions:string, cookingTime:string  }[];
  isModalOpen = false;
  currentObject :any
  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  constructor() { }

  ngOnInit() {}

  setCurrent(o : any) {
    this.currentObject = o;
  }

}
