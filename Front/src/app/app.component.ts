import { Component } from '@angular/core';
import {JwtService} from "./service/jwt.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'jwt-angular';
  constructor(public jwtService: JwtService, private router: Router) { }

  logout(): void {
    this.jwtService.logout();
    this.router.navigate(['/login']);
  }
}
