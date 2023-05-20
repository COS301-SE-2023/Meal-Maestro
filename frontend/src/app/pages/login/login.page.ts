import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
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

  constructor( private router: Router ) { }

  ngOnInit() {
  }

  login() {
    this.router.navigate(['app/tabs/home']);
    console.log(this.user);
  }
}
