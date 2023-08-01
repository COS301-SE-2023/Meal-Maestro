import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { UserI } from '../../models/user.model';

@Component({
  selector: 'app-acc-profile',
  templateUrl: './acc-profile.page.html',
  styleUrls: ['./acc-profile.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AccProfilePage implements OnInit {

  user: UserI;

  
  constructor(private router: Router, private auth: AuthenticationService) {
    this.user = {
      username: '',
      email: '',
      password: ''
    };
   }
  
  ngOnInit() {
    this.auth.getUser().subscribe({
      next: (response) => {
        if (response.status == 200) {
          if (response.body && response.body.name) {
            this.user.username = response.body.name;
            this.user.email = response.body.email;
            this.user.password = response.body.password;
          }
        }
      },
      error: (error) => {
        console.log(error);
      }
    })
  }
  
  goBack() {
    this.router.navigate(['app/tabs/profile'])
  }

  logout() {
    this.auth.logout();
  }

}
