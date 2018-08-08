import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { LoginModel } from '../models/login-model';
import { Router } from '../../../node_modules/@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private router: Router) { }

  login(loginData: LoginModel, route: string): void {
    // TODO: Make request to BE
    /* this.httpClient.get('/api/login', { params: { username: username, password: password } }).subscribe((res) => {
       const jwt = res;
       console.log(jwt);
       window.localStorage['jwtToken'] = res;
     }, (err) => {
       console.error(err);
     });*/
    window.localStorage['token'] = loginData.username + '-' + loginData.password;
    this.router.navigate([route]);
  }

  logout(): void {
    window.localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    // TODO: Check if token has expired
    const token = localStorage.getItem('token');
    return token ? true : false;
  }
}
