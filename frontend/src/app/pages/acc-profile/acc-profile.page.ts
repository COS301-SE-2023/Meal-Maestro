import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ViewWillEnter } from '@ionic/angular';
import { Router } from '@angular/router';
import {
  AuthenticationService,
  ErrorHandlerService,
} from '../../services/services';
import { UserI } from '../../models/user.model';
import { UserApiService } from '../../services/user-api/user-api.service';

@Component({
  selector: 'app-acc-profile',
  templateUrl: './acc-profile.page.html',
  styleUrls: ['./acc-profile.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
})
export class AccProfilePage implements OnInit, ViewWillEnter {
  user: UserI;
  usernameButtons = [
    {
      text: 'Cancel',
      role: 'cancel',
    },
    {
      text: 'Confirm',
      role: 'confirm',
    },
  ];
  usernameInputs = [
    {
      placeholder: 'Username',
      type: 'text',
    },
  ];

  constructor(
    private router: Router,
    private auth: AuthenticationService,
    private userApi: UserApiService,
    private errorHandler: ErrorHandlerService
  ) {
    this.user = {
      username: '',
      email: '',
      password: '',
    };
  }
  ionViewWillEnter(): void {
    this.getUserInfo();
  }

  ngOnInit() {}

  async getUserInfo() {
    this.auth.getUser().subscribe({
      next: (response) => {
        if (response.status == 200) {
          if (response.body && response.body.name) {
            this.user.username = response.body.name;
            this.user.email = response.body.email;
            this.user.password = '';
          }
        }
      },
      error: (error) => {
        if (error.status == 403) {
          this.errorHandler.presentErrorToast('You are not logged in.', error);
          this.auth.logout();
        }
      },
    });
  }

  onUsernameChange(event: any) {
    const role = event.detail.role;
    if (role == 'confirm') {
      this.user.username = event.detail.data.values[0];
      this.userApi.updateUsername(this.user).subscribe({
        next: (response) => {
          if (response.status == 200) {
            this.errorHandler.presentSuccessToast('Username updated.');
            if (response.body) {
              this.user.name = response.body.name;
              this.user.email = response.body.email;
            }
          }
        },
      });
    }
  }

  goBack() {
    this.router.navigate(['app/tabs/profile']);
  }

  logout() {
    this.auth.logout();
  }
}
