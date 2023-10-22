import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { DaysMealsI, MealI } from '../../models/interfaces';

@Component({
  selector: 'app-daily-meals-tutorial',
  templateUrl: './daily-meals-tutorial.component.html',
  styleUrls: ['./daily-meals-tutorial.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DailyMealsTutorialComponent implements OnInit {
  constructor() {}

  breakfast: string = 'breakfast';
  lunch: string = 'lunch';
  dinner: string = 'dinner';
  mealDay: string | undefined;
  @Input() dayData!: DaysMealsI;
  isBreakfastModalOpen = false;
  isLunchModalOpen = false;
  isDinnerModalOpen = false;
  isModalOpen = false;
  currentObject: DaysMealsI | undefined;
  isBreakfastLoading: boolean = false;
  isLunchLoading: boolean = false;
  isDinnerLoading: boolean = false;
  item: MealI | undefined;
  fIns: String[] = [];
  fIng: String[] = [];
  @Input() items!: MealI[];

  ngOnInit() {}
}
