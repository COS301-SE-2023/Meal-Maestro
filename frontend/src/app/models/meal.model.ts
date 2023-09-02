export interface MealI {
  name: string;
  description: string;
  image: string;
  ingredients: string;
  instructions: string;
  cookingTime: string;
  type: 'breakfast' | 'lunch' | 'dinner';
}
