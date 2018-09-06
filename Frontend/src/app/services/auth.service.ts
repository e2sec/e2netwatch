import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { LoginModel } from '../models/login-model';
import { Router } from '../../../node_modules/@angular/router';
import { Observable } from '../../../node_modules/rxjs';
import { UserProfileService } from './user-profile.service';
import { UserProfile } from '../models/user-profile';
import * as jwt_decode from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private router: Router, private userProfileService: UserProfileService) { }

  login(loginData: LoginModel): Observable<any> {
    return this.httpClient.post<LoginModel>(`${environment.restApiUrl}middleware/api/auth/login`, loginData);
  }

  getToken(): string {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (!token) { return false; }
    if (this.isTokenExpired(token)) { return false; }

    return true;
  }

  getTokenExpirationDate(token: string): Date {
    const decoded = jwt_decode(token);

    if (decoded.exp === undefined) { return null; }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  isTokenExpired(token?: string): boolean {
    if (!token) { token = localStorage.getItem('token'); }
    if (!token) { return true; }

    const date = this.getTokenExpirationDate(token);
    if (date === undefined) { return false; }
    return !(date.valueOf() > new Date().valueOf());
  }
}
