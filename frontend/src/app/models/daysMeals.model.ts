import { MealI } from "./meal.model";

export interface DaysMealsI {
    breakfast:MealI;
    lunch:MealI;
    dinner:MealI;
    mealDate:string;
}