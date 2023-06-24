import { Injectable } from '@angular/core';
import { ToastController } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private toastController: ToastController) { }

  async presentErrorToast(message: string, error: any) {
    const toast = await this.toastController.create({
      message: message,
      duration: 2000,
      color: 'danger',
      position: 'top',
      icon: 'alert-circle-outline'
    });
    toast.present();
    console.error(error);
  }
}
