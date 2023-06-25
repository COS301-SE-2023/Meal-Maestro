import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { AuthenticationService, ErrorHandlerService } from '../../services/services';
import { UserI } from '../../models/interfaces';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class SignupPage {
  initial: string = '';
  verify: string = '';
  user: UserI = {
    username: '',
    password: '',
    email: '',
  }

  constructor(private router: Router, private errorHandlerService: ErrorHandlerService, private auth: AuthenticationService ) { }

  async signup(form: any) {
    console.log(form);
    if (form.initial !== form.verify) {
      this.errorHandlerService.presentErrorToast('Passwords do not match', 'Passwords do not match');
      return;
    }

    const newUser: UserI = {
      username: form.username,
      password: form.verify,
      email: form.email,
    }

    this.auth.checkUser(newUser).subscribe({
      next: data => {
        if (data) {
          this.errorHandlerService.presentErrorToast('Username or email already exists', 'Username or email already exists');
        } else {
          this.auth.createUser(newUser).subscribe({
            next: () => {
            this.errorHandlerService.presentSuccessToast('Signup successful');
            this.router.navigate(['app/tabs/home']);
            },
            error: error => {
            this.errorHandlerService.presentErrorToast('Signup failed', error);
            }
          });
        }
      },
      error: error => {
        this.errorHandlerService.presentErrorToast('Signup failed', error);
      }
    });
  }

  goToLogin() {
    this.router.navigate(['../']);
  }
}
