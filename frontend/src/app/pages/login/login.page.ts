import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { AuthenticationService, ErrorHandlerService } from '../../services/services';
import { UserI } from '../../models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class LoginPage {
  user: UserI = {
    username: '',
    email: '',
    password: '',
  }
  

  constructor( private router: Router, private errorHandlerService: ErrorHandlerService, private auth: AuthenticationService ) { }

  login(form: any) {

    const loginUser: UserI = {
      username: '',
      email: form.email,
      password: form.password,
    }
    this.auth.login(loginUser).subscribe({
      next: (result) => {
        if (result) {
          this.auth.getUser(loginUser.email).subscribe({
            next: (user) => {
              localStorage.setItem('user', user.username);
              localStorage.setItem('email', user.email);
            },
            error: error => {
              this.errorHandlerService.presentErrorToast('Login failed', error);
            }
          });

          this.errorHandlerService.presentSuccessToast('Login successful');
          this.router.navigate(['app/tabs/home']);
        }
        else {
          this.errorHandlerService.presentErrorToast('Invalid credentials', 'Invalid credentials');
        }
      },
      error: error => {
        this.errorHandlerService.presentErrorToast('Login failed', error);
      }
    });
  }

  goToSignup() {
    this.router.navigate(['../signup']);
  }
}
