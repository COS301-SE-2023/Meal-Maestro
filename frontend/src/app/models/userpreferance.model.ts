export interface UserPreferencesI {
    goal: string;
    shopping_interval: string;
    food_preferences: string[];
    calorie_amount: number;
    budget_range: string;
    macro_ratio: {protien: number, carbs: number, fats: number};
    allergies: string[];
    cooking_time: number;
    user_height: number;
    user_weight: number;
    user_BMI: number;
  }