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
  
  private swiper: Swiper;
  @ViewChild('videoPlayer') videoPlayer!: ElementRef;

  constructor(private modalController: ModalController, private errorHandlerService: ErrorHandlerService) { }

  ngOnInit() {}

  ngAfterViewInit() {
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
      // on: {
      //   slideChange: () => {
      //     if (this.swiper.isEnd) {
      //       this.showPopup();
      //     }
      //   },
      // },
    });
  }

  playVideo() {
    const video: HTMLVideoElement = this.videoPlayer.nativeElement;
    video.play();
  }

  pauseVideo() {
    const video: HTMLVideoElement = this.videoPlayer.nativeElement;
    video.pause();
  }

  onSwiper(swiper: Swiper) {
    this.swiper = swiper;
  }

  // onSlideChange() {
  //   if (this.swiper.isEnd) {
  //     //this.errorHandlerService.presentSuccessToast('Registration successful');
  //     this.showPopup();
  //   }
  // }

  closeModal() {
    this.modalController.dismiss();
  }

 
  
  



}
