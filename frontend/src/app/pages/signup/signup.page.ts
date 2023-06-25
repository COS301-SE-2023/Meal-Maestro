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
    this.router.navigate(['app/tabs/home']);
    console.log(form);
  }

  goToLogin() {
    this.router.navigate(['../']);
  }
}
