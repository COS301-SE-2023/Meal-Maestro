export interface UserPreferencesI {
    goal: string;
    shopping_interval: string;
    food_preferences: string[];
    calorie_amount: number;
    budget_range: string;
    macro_ratio: {protein: number, carbs: number, fats: number};
    allergies: string[];
    cooking_time: number;
    user_height: number; //consider moving to account
    user_weight: number; //consider moving to account
    user_BMI: number;

    BMI_set : boolean;
    cookingtime_set : boolean;
    allergies_set : boolean;
    macro_set : boolean;
    budget_set : boolean;
    calorie_set : boolean;
    foodpreferance_set : boolean;
    shoppinginterfval_set : boolean;

    
  }