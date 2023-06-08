import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class LoginPage implements OnInit {

  user = {
    email: '',
    password: ''
  };

  constructor( private router: Router, private toastController : ToastController  ) { }

  ngOnInit() {
  }

  login() {
    this.router.navigate(['app/tabs/home']);
    console.log(this.user);
    this.loginSuccessToast('top');
  }

  signup() {
    this.router.navigate(['../signup']);
    console.log(this.user);
    // this.loginSuccessToast('top');
  }

  async loginSuccessToast(position: 'bottom' | 'middle' | 'top'){
    const toast = await this.toastController.create({
      message: "Login Successful",
      duration: 1500,
      position: position,
      color: 'success',
      icon: 'checkmark-sharp'
    });
    toast.present();
  }

  async loginFailToast(position: 'bottom' | 'middle' | 'top'){
    const toast = await this.toastController.create({
      message: "Login Failed",
      duration: 1500,
      position: position,
      color: 'danger',
      icon: 'close-sharp'
    });
    toast.present();
  }
}
