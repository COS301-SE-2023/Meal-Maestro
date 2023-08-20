export interface SettingsI {
  goal: string;
  shoppingInterval: string;
  foodPreferences: string[];
  calorieAmount: number;
  budgetRange: string;
  macroRatio: { protein: number; carbs: number; fat: number };
  allergies: string[];
  cookingTime: string;
  userHeight: number; //consider moving to account
  userWeight: number; //consider moving to account
  userBMI: number;

  bmiset: boolean;
  cookingTimeSet: boolean;
  allergiesSet: boolean;
  macroSet: boolean;
  budgetSet: boolean;
  calorieSet: boolean;
  foodPreferenceSet: boolean;
  shoppingIntervalSet: boolean;
}
