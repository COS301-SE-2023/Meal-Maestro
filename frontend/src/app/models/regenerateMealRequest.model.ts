import { MealI } from './interfaces';

export interface RegenerateMealRequestI {
  meal: MealI | undefined;
  mealDate: Date | undefined;
}
