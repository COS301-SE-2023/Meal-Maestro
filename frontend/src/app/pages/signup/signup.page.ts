import { Component, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import {
  AuthenticationService,
  ErrorHandlerService,
} from '../../services/services';
import { UserI } from '../../models/interfaces';
import { TutorialComponent } from '../../components/tutorial/tutorial.component';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
})
export class SignupPage {
  initial: string = '';
  verify: string = '';
  user: UserI = {
    username: '',
    password: '',
    email: '',
  };

  constructor(
    private router: Router,
    private errorHandlerService: ErrorHandlerService,
    private auth: AuthenticationService,
    private modalController: ModalController,
    private ngZone: NgZone
  ) {}

  async signup(form: any) {
    if (form.initial !== form.verify) {
      this.errorHandlerService.presentErrorToast(
        'Passwords do not match',
        'Passwords do not match'
      );
      return;
    }

    const newUser: UserI = {
      username: form.username,
      password: form.verify,
      email: form.email,
    };

    this.auth.register(newUser).subscribe({
      next: (response) => {
        if (response.status == 200) {
          if (response.body) {
            this.auth.setToken(response.body.token);
            this.errorHandlerService.presentSuccessToast(
              'Registration successful'
            );
            this.openModal();
            this.router.navigate(['app/tabs/home']);
          }
        }
      },
      error: (error) => {
        if (error.status == 400) {
          this.errorHandlerService.presentErrorToast(
            'Email already exists',
            'Email already exists'
          );
        } else {
          this.errorHandlerService.presentErrorToast(
            'Unexpected error. Please try again',
            error
          );
        }
      },
    });
  }
  async openModal() {
    this.ngZone.run(async () => {
      const modal = await this.modalController.create({
        component: TutorialComponent,
      });
      await modal.present();
    });
  }

  goToLogin() {
    this.router.navigate(['../']);
    localStorage.removeItem('token');
  }
}
