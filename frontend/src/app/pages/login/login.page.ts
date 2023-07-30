import { Component, OnInit } from '@angular/core';
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
export class LoginPage implements OnInit {
  user: UserI = {
    username: '',
    email: '',
    password: '',
  }
  

  constructor(private router: Router,
              private errorHandlerService: ErrorHandlerService, 
              private auth: AuthenticationService 
              ) { }

  ngOnInit() {
  }

  async login(form: any) {
    const loginUser: UserI = {
      username: '',
      email: form.email,
      password: form.password,
    }
    this.auth.login(loginUser).subscribe({
      next: (response) => {
        if (response.status == 200) {
          if (response.body) {
            this.auth.setToken(response.body.token);
            this.errorHandlerService.presentSuccessToast('Login successful');
            this.router.navigate(['app/tabs/home']);
          }
        }
      },
      error: (error) => {
        if (error.status == 403){
          this.errorHandlerService.presentErrorToast('Invalid credentials', 'Invalid credentials');
          localStorage.removeItem('token');
        }else if(error.status == 404){
          this.errorHandlerService.presentErrorToast('Email or password incorrect', 'Email or password incorrect');
          localStorage.removeItem('token');
        }else{
          this.errorHandlerService.presentErrorToast('Unexpected error. Please try again', error);
        }
      }
    });
  }

  goToSignup() {
    this.router.navigate(['../signup']);
    localStorage.removeItem('token');
  }
}
