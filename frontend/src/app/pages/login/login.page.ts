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
    password: '',
    email: '',
  }
  

  constructor( private router: Router, private errorHandlerService: ErrorHandlerService, private auth: AuthenticationService ) { }

  login(form: any) {

    const loginUser: UserI = {
      username: '',
      password: form.password,
      email: form.email,
    }

    this.auth.login(loginUser).subscribe({
      next: (result) => {
        if (result) {
          this.errorHandlerService.presentSuccessToast('Login successful');
          
          this.router.navigate(['app/tabs/home']);
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
