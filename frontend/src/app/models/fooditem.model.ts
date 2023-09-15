export interface FoodItemI {
  name: string;
  quantity: number | null;
  unit: 'kg' | 'g' | 'l' | 'ml' | 'pcs' | undefined;
  id?: number;
  price?: number;
}
