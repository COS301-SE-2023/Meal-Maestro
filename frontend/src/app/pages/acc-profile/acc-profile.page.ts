import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';

@Component({
  selector: 'app-acc-profile',
  templateUrl: './acc-profile.page.html',
  styleUrls: ['./acc-profile.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule]
})
export class AccProfilePage implements OnInit {

  goBack() {
    this.router.navigate(['app/tabs/profile'])
  }

  constructor(private router: Router) { }

  ngOnInit() {
  }

  logout() {
    this.router.navigate(['../']);
    localStorage.clear();
  }

}
