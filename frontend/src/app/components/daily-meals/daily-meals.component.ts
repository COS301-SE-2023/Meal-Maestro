import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  Input,
  ViewChildren,
  QueryList,
  Renderer2,
  ElementRef,
} from '@angular/core';
import { IonItemSliding, IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { MealGenerationService } from '../../services/meal-generation/meal-generation.service';
import { DaysMealsI } from '../../models/daysMeals.model';
import { ErrorHandlerService } from '../../services/services';
import { MealI, RegenerateMealRequestI } from '../../models/interfaces';
import { AddRecipeService } from '../../services/recipe-book/add-recipe.service';

@Component({
  selector: 'app-daily-meals',
  templateUrl: './daily-meals.component.html',
  styleUrls: ['./daily-meals.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DailyMealsComponent implements OnInit {
  @ViewChildren(IonItemSliding) slidingItems!: QueryList<IonItemSliding>;
  breakfast: string = 'breakfast';
  lunch: string = 'lunch';
  dinner: string = 'dinner';
  mealDay: string | undefined;
  @Input() dayData!: DaysMealsI;
  isBreakfastModalOpen = false;
  isLunchModalOpen = false;
  isDinnerModalOpen = false;
  isModalOpen = false;
  currentObject: DaysMealsI | undefined;
  isBreakfastLoading: boolean = false;
  isLunchLoading: boolean = false;
  isDinnerLoading: boolean = false;
  item: MealI | undefined;
  fIns: String[] = [];
  fIng: String[] = [];
  @Input() items!: MealI[];

  constructor(
    public r: Router,
    private mealGenerationservice: MealGenerationService,
    private errorHandlerService: ErrorHandlerService,
    private addService: AddRecipeService,
    private renderer: Renderer2,
    private el: ElementRef
  ) {}

  setOpen(isOpen: boolean, mealType: string) {
    if (mealType === 'breakfast') {
      this.item = this.dayData.breakfast;         
      this.isModalOpen = isOpen;
      if (isOpen) {
        this.setCurrent(this.dayData?.breakfast);
      }
    } else if (mealType === 'lunch') {
      this.item = this.dayData.lunch;      
      this.isModalOpen = isOpen;
      if (isOpen) {
        this.setCurrent(this.dayData?.lunch);
      }
    } else if (mealType === 'dinner') {
      this.isModalOpen = isOpen;
      this.item = this.dayData.dinner;
      if (isOpen) {
        this.setCurrent(this.dayData?.dinner);
      }
    }

    if (isOpen) {      
      this.formatIns(this.item!.instructions);
      this.formatIng(this.item!.ingredients);   
    }
  }

  addRecipe(item: MealI) {
    this.addService.setRecipeItem(item);
  }

  private formatIns(ins: string) {
    const insArr: string[] = ins.split(/\d+\.\s+/);
    this.fIns = insArr.filter(instruction => instruction.trim() !== '');
  }

  private formatIng(ing: string) {
    const ingArr: string[] = ing.split(/,[^()]*?(?![^(]*\))/);
    this.fIng = ingArr.map((ingredient) => ingredient.trim());
  }

  notSaved(): boolean {
    return !this.items.includes(this.item!);
  } 

  ngOnInit() {
    console.log(this.dayData);
    if (this.item && this.item.instructions) {
      this.formatIns(this.item.instructions);
    }

    if (this.item && this.item.ingredients) {
      this.formatIng(this.item.ingredients);
    }
  }

  handleArchive(meal: string) {
    // Function to handle the "Archive" option action
    var recipe: MealI | undefined;

    if (meal == 'breakfast') recipe = this.dayData.breakfast;
    else if (meal == 'lunch') recipe = this.dayData.lunch;
    else recipe = this.dayData.dinner;

    console.log('button clicked');

    this.closeItem();
    this.addService.setRecipeItem(recipe);
  }

  async handleRegenerate(meal: MealI | undefined, mealDate: Date | undefined) {
    // Function to handle the "Sync" option action
    if (meal && mealDate) {
      this.closeItem();
      if (meal.type == 'breakfast') this.isBreakfastLoading = true;
      else if (meal.type == 'lunch') this.isLunchLoading = true;
      else this.isDinnerLoading = true;

      let regenRequest: RegenerateMealRequestI = {
        meal: meal,
        mealDate: mealDate,
      };
      this.fetchLoadingSvg(meal.type);
      this.mealGenerationservice.regenerate(regenRequest).subscribe({
        next: (data) => {
          if (data.body) {
            console.log(data.body);
            if (meal.type == 'breakfast') {
              this.isBreakfastLoading = false;
              this.dayData.breakfast = data.body;
            } else if (meal.type == 'lunch') {
              this.isLunchLoading = false;
              this.dayData.lunch = data.body;
            } else if (meal.type == 'dinner') {
              this.isDinnerLoading = false;
              this.dayData.dinner = data.body;
            }
          }
        },
        error: (err) => {
          this.errorHandlerService.presentErrorToast(
            'Error regenerating meal items',
            err
          );
        },
      });
    }
  }

  setCurrent(o: any) {
    this.currentObject = o;
  }

  closeItem() {
    this.slidingItems.forEach((item: IonItemSliding) => {
      item.close();
    });
  }

  fetchLoadingSvg(name: string) {
    fetch('assets/regen.svg')
      .then((response) => response.text())
      .then((svg) => {
        this.renderer.setProperty(
          this.el.nativeElement.querySelector('.' + name + '-svg-container'),
          'innerHTML',
          svg
        );
        return;
      });
  }
}
