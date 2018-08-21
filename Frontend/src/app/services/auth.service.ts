import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { LoginModel } from '../models/login-model';
import { Router } from '../../../node_modules/@angular/router';
import { Observable } from '../../../node_modules/rxjs';
import { UserProfileService } from './user-profile.service';
import { UserProfile } from '../models/user-profile';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, private router: Router, private userProfileService: UserProfileService) { }

  login(loginData: LoginModel): Observable<any> {
    // TODO: Make request to BE
    /* this.httpClient.get('/api/login', { params: { username: username, password: password } }).subscribe((res) => {
       const jwt = res;
       console.log(jwt);
       window.localStorage['jwtToken'] = res;
     }, (err) => {
       console.error(err);
     });*/
    // window.localStorage['token'] = loginData.username + '-' + loginData.password;
    /*this.userProfileService.setUserProfile({
      username: 'Nikola',
      avatarUrl: 'https://www.gravatar.com/avatar/78c63138f5a54617819e47926b5977d7'
    });
*/
    return this.httpClient.post<LoginModel>('http://localhost:1337/login', loginData);
  }

  isAuthenticated(): boolean {
    // TODO: Check if token has expired
    const token = localStorage.getItem('token');
    return token ? true : false;
  }
}
