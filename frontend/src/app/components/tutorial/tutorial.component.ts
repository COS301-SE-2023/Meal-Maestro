import { Component, ViewChild, ElementRef,  CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { IonicModule } from '@ionic/angular';
// import { SwiperContainer } from 'swiper/element';
// import { Navigation } from '@angular/router';
// import { register } from 'swiper/element/bundle';
//import { SwiperModule } from 'swiper/angular';
import { CommonModule } from '@angular/common';
// import Swiper from 'swiper';
//import { Navigation, Pagination } from 'swiper/modules';
// register();

//import { Navigation, Pagination } from 'swiper';


//SwiperCore.use([Navigation, Pagination]);

@Component({
  
  selector: 'app-tutorial',
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.scss'],
  standalone:true,
  imports: [CommonModule, IonicModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class TutorialComponent  {

  // @ViewChild('swiperContainer') swiperContainer!: ElementRef;
  // swiperConfig = {
  //   pagination: {
  //     el: '.swiper-pagination',
  //     type: 'progressbar',
  //   },
  //   navigation: {
  //     nextEl: '.swiper-button-next',
  //     prevEl: '.swiper-button-prev',
  //   },
  // };

  constructor(private modalController: ModalController) { }

  closeModal() {
    this.modalController.dismiss();
  }



}
