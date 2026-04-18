import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  title = 'Welcome to E-Commerce';
  description = 'This is a full-stack e-commerce application built with Spring Boot backend and Angular frontend.';
}
