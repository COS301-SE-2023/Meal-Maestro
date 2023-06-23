import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SignupPage implements OnInit {

  user = {
    email: '',
    password: ''
  };

  constructor(private router: Router, private toastController : ToastController ) { }

  ngOnInit() {
  }

  signup() {
    this.router.navigate(['app/tabs/home']);
    console.log(this.user);
    this.signupSuccessToast('top');
  }

  goToLogin() {
    this.router.navigate(['../']);
  }

  async signupSuccessToast(position: 'bottom' | 'middle' | 'top'){
    const toast = await this.toastController.create({
      message: "Sign Up Successful",
      duration: 1500,
      position: position,
      color: 'success',
      icon: 'checkmark-sharp'
    });
    toast.present();
  }

  async signupFailToast(position: 'bottom' | 'middle' | 'top'){
    const toast = await this.toastController.create({
      message: "Sign Up Failed",
      duration: 1500,
      position: position,
      color: 'danger',
      icon: 'close-sharp'
    });
    toast.present();
  }

}
