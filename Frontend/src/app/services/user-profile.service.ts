import { Injectable } from '@angular/core';
import { UserProfile } from '../models/user-profile';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  constructor() { }

  private userProfileSource = new BehaviorSubject(new UserProfile);
  currentUserProfile = this.userProfileSource.asObservable();

  setUserProfile(userProfile: UserProfile) {
    this.userProfileSource.next(userProfile);
  }
}
