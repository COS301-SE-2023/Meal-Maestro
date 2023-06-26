export interface UserPreferencesI {
    goal: string;
    shopping_interval: string;
    food_preferences: string[];
    calorie_amount: number | null;
    budget_range: string;
    macro_ratio: {protein: number | null, carbs: number | null, fats: number | null};
    allergies: string[];
    cooking_time: number | null;
    user_height: number | null; //consider moving to account
    user_weight: number | null; //consider moving to account
    user_BMI: number | null;

    BMI_set : boolean;
    cookingtime_set : boolean;
    allergies_set : boolean;
    macro_set : boolean;
    budget_set : boolean;
    calorie_set : boolean;
    foodpreference_set : boolean;
    shoppinginterfval_set : boolean;

    
  }