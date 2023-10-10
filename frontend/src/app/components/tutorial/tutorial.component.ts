import {
  Component,
  ViewChild,
  ElementRef,
  OnInit,
  AfterViewInit,
  CUSTOM_ELEMENTS_SCHEMA,
} from '@angular/core';
import { ModalController } from '@ionic/angular';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import Swiper from 'swiper';
import { DaysMealsI, MealI } from '../../models/interfaces';
import { DailyMealsComponent } from '../daily-meals/daily-meals.component';
import { RecipeItemComponent } from '../recipe-item/recipe-item.component';

@Component({
  selector: 'app-tutorial',
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    DailyMealsComponent,
    RecipeItemComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class TutorialComponent implements OnInit, AfterViewInit {
  private swiper: Swiper = {} as Swiper;
  @ViewChild('swiper') swiperElement!: ElementRef;
  @ViewChild('videoPlayer') videoPlayer!: ElementRef;

  foods: MealI[] = [];
  meals: DaysMealsI[] = [
    {
      breakfast: {
        name: 'Sausage & Cheese Quiche',
        description: '',
        image:
          'https://images.unsplash.com/photo-1608855238293-a8853e7f7c98?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80',
        ingredients: '',
        instructions: '',
        cookingTime: '',
        type: 'breakfast',
      },
      lunch: {
        name: 'Grilled Chicken Salad',
        description: '',
        image:
          'https://images.unsplash.com/photo-1604909052743-94e838986d24?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80',
        ingredients: '',
        instructions: '',
        cookingTime: '',
        type: 'lunch',
      },
      dinner: {
        name: 'Chicken Parmesan',
        description: '',
        image: '',
        ingredients: '',
        instructions: '',
        cookingTime: '',
        type: 'dinner',
      },
      mealDay: 'Monday',
      mealDate: new Date(),
    },
  ];
  items: MealI[] = [
    {
      name: 'Sausage & Cheese Quiche',
      description: '',
      image:
        'https://images.unsplash.com/photo-1608855238293-a8853e7f7c98?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80',
      ingredients: '',
      instructions: '',
      cookingTime: '',
      type: 'breakfast',
    },
    {
      name: 'Grilled Chicken Salad',
      description: '',
      image:
        'https://images.unsplash.com/photo-1604909052743-94e838986d24?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1480&q=80',
      ingredients: '',
      instructions: '',
      cookingTime: '',
      type: 'lunch',
    },
  ];

  constructor(private modalController: ModalController) {}

  ngOnInit() {}

  ngAfterViewInit() {
    //console.log('called');
    this.swiper = new Swiper('.swiper-container', {
      // Swiper configuration options
      pagination: {
        el: '.swiper-pagination',
        type: 'progressbar',
      },
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
      },
      on: {
        slideChange: () => {
          console.log('slides started');
          this.onSlideChange();
        },
        slideChangeTransitionStart: () => {
          console.log('transition started');
          this.onSlideChangeTransitionStart();
        },
      },
    });
    // this.swiper.on('slideChange', () => {
    //   console.log("IN FUNCTION")
    //   this.onSlideChange();
    // })
    // console.log('done');
  }

  onSlideChange() {
    //if (this.swiper.isEnd) {
    const video: HTMLVideoElement = this.videoPlayer.nativeElement;
    const activeSlide = this.swiper.slides[this.swiper.activeIndex];
    if (activeSlide.classList.contains('swip')) {
      // If the active slide contains the video, play it
      video.play();
    } else {
      // If the active slide doesn't contain the video, pause it
      video.pause();
    }
    //video.pause();
    // }
  }

  onSlideChangeTransitionStart() {
    console.log('slide started');
    const video: HTMLVideoElement = this.videoPlayer.nativeElement;
    video.currentTime = 0;
  }

  onSwiper(swiper: Swiper) {
    this.swiper = swiper;
  }

  closeModal() {
    this.modalController.dismiss();
  }
}
