import { MealI } from './meal.model';

export interface DaysMealsI {
  breakfast: MealI | undefined;
  lunch: MealI | undefined;
  dinner: MealI | undefined;
  mealDay: string | undefined;
  mealDate: Date | undefined;
}
