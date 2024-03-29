import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { MealI } from '../../models/meal.model';
import { CommonModule } from '@angular/common';
import { AuthenticationService, ErrorHandlerService, LikeDislikeService } from '../../services/services';

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.scss'],
  standalone: true,

  imports: [IonicModule, CommonModule]

})
export class RecipeItemComponent {
  items: MealI[] = [];
  item!: MealI | undefined;
  fIns: string[] = [];
  fIng: string[] = [];
  modalOpen: Boolean = false;

  openModal(item: MealI) {
    this.item = item;
    this.formatIns(this.item.instructions);
    this.formatIng(this.item.ingredients);
    this.modalOpen = true;
  }

  public passItems(items: MealI[]): void {
    this.items = items;
  }

  constructor(private likeDislikeService: LikeDislikeService,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService) { }

  ngOnInit() {
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

  closeModal() {
    this.modalOpen = false;
  }

  notSaved(): boolean {
    return !this.items.includes(this.item!);
  } 

  async liked(item: MealI) {
    this.likeDislikeService.liked(item).subscribe({
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorised access. Please log in again',
            err
          );
          this.auth.logout();
        }
      }
    });
  }

  async disliked(item: MealI) {
    this.likeDislikeService.disliked(item).subscribe({
      error: (err) => {
        if (err.status === 403) {
          this.errorHandlerService.presentErrorToast(
            'Unauthorised access. Please log in again',
            err
          );
          this.auth.logout();          
        }
      }
    });
  }
}
