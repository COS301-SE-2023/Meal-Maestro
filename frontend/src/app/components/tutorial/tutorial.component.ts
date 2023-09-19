import { Component, ViewChild, ElementRef,OnInit, AfterViewInit , CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ModalController } from '@ionic/angular';
import {IonicModule } from '@ionic/angular';
import { AuthenticationService, ErrorHandlerService } from '../../services/services';
import { CommonModule } from '@angular/common';
import Swiper from 'swiper';


@Component({
  
  selector: 'app-tutorial',
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class TutorialComponent implements OnInit, AfterViewInit {
  
  private swiper: Swiper = {} as Swiper;
  @ViewChild('swiper') swiperElement!: ElementRef;
  @ViewChild('videoPlayer') videoPlayer!: ElementRef;

  constructor(private modalController: ModalController, private errorHandlerService: ErrorHandlerService) { }

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
    console.log('slide changed');
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
