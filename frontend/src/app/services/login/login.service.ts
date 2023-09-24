import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private homeRefreshed: boolean = false;
  private pantryRefreshed: boolean = false;
  private recipeBookRefreshed: boolean = false;
  private settingsRefreshed: boolean = false;
  private shoppingLocation: string | '' = '';

  constructor() {}

  isHomeRefreshed(): boolean {
    return this.homeRefreshed;
  }

  setHomeRefreshed(refreshed: boolean): void {
    this.homeRefreshed = refreshed;
  }

  isPantryRefreshed(): boolean {
    return this.pantryRefreshed;
  }

  setPantryRefreshed(refreshed: boolean): void {
    this.pantryRefreshed = refreshed;
  }

  isRecipeBookRefreshed(): boolean {
    return this.recipeBookRefreshed;
  }

  setRecipeBookRefreshed(refreshed: boolean): void {
    this.recipeBookRefreshed = refreshed;
  }

  isSettingsRefreshed(): boolean {
    return this.settingsRefreshed;
  }

  setSettingsRefreshed(refreshed: boolean): void {
    this.settingsRefreshed = refreshed;
  }

  isShoppingAt(): string {
    return this.shoppingLocation;
  }

  setShoppingAt(location: string): void {
    this.shoppingLocation = location;
  }

  resetRefreshed(): void {
    this.homeRefreshed = false;
    this.pantryRefreshed = false;
    this.recipeBookRefreshed = false;
    this.settingsRefreshed = false;
    this.shoppingLocation = '';
  }

  toString(): string {
    return (
      'homeRefreshed: ' +
      this.homeRefreshed +
      ', pantryRefreshed: ' +
      this.pantryRefreshed +
      ', recipeBookRefreshed: ' +
      this.recipeBookRefreshed +
      ', settingsRefreshed: ' +
      this.settingsRefreshed
    );
  }
}
