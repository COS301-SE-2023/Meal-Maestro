import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-tutorial',
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.scss'],
  standalone:true,
  imports: [IonicModule],
})
export class TutorialComponent  {

  constructor(private modalController: ModalController) { }

  closeModal() {
    this.modalController.dismiss();
  }



}
