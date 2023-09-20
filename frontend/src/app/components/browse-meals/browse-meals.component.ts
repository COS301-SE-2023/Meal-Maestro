import { CommonModule } from '@angular/common';
import { Component, OnInit, Input } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealI } from '../../models/interfaces';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';
import { AuthenticationService, ErrorHandlerService, LoginService, RecipeBookApiService } from '../../services/services';

@Component({
  selector: 'app-browse-meals',
  templateUrl: './browse-meals.component.html',
  styleUrls: ['./browse-meals.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
})

export class BrowseMealsComponent  implements OnInit {
  @Input() mealsData!: MealI;
  @Input() searchData!: MealI;
  @Input() Searched: boolean = false;

  item: MealI | undefined;
  popularMeals: MealI[] = [];
  thing: MealI | undefined;
  searchedMeals: MealI[] = [];
  isModalOpen = false;
  currentObject: any;
  fIns: String[] = [];
  fIng: String[] = [];
  @Input() items!: MealI[];

  constructor(
    private loginService: LoginService,
    private recipeService: RecipeBookApiService,
    private auth: AuthenticationService,
    private errorHandlerService: ErrorHandlerService) { }

  ngOnInit() {
   // console.log(this.mealsData);
    this.item = this.mealsData;

    if (this.item && this.item.instructions) {
      this.formatIns(this.item.instructions);
    }

    if (this.item && this.item.ingredients) {
      this.formatIng(this.item.ingredients);
    }
  }

  private formatIns(ins: string) {
    const insArr: string[] = ins.split(/\d+\.\s+/);
    this.fIns = insArr.filter(instruction => instruction.trim() !== '');
  }

  private formatIng(ing: string) {
    const ingArr: string[] = ing.split(/,[^()]*?(?![^(]*\))/);
    this.fIng = ingArr.map((ingredient) => ingredient.trim());
  }

  async addRecipe(item: MealI) {
    this.recipeService.addRecipe(item).subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.getRecipes();
            this.loginService.setRecipeBookRefreshed(false);
            this.errorHandlerService.presentSuccessToast(
              item.name + ' added to Recipe Book'
            );
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorised access. Please log in again.',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error adding item to your Recipe Book',
            err
          );
        }
      },
    });
  }

  notSaved(): boolean {
    return !this.items.includes(this.item!);
  } 

  setCurrent(o : any) {
    this.currentObject = o;
  }

  setOpen(isOpen: boolean, o :any) {
    if(o==null)
      o = this.currentObject
    this.isModalOpen = isOpen;
    this.setCurrent(o)
  }
  
  async getRecipes() {
    this.recipeService.getAllRecipes().subscribe({
      next: (response) => {
        if (response.status === 200) {
          if (response.body) {
            this.items = response.body;
          }
        }
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorised access. Please log in again',
            err
          );
          this.auth.logout();
        } else {
          this.errorHandlerService.presentErrorToast(
            'Error loading saved recipes',
            err
          );
        }
      },
    });
  }
}
