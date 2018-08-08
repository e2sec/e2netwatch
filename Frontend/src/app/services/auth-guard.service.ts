import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '../../../node_modules/@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.authService.logout();
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }

  canLoad(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.authService.logout();
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}
