import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../../models/user-profile';
import { AppState, selectProfileState } from '../../../store/app.states';
import { Store, select } from '@ngrx/store';
import { Logout } from '../../../store/actions/auth.actions';
import { Observable } from 'rxjs';
import { ProfileState } from '../../../store/reducers/profile.reducers';
import { LoadProfile } from '../../../store/actions/profile.actions';

@Component({
  selector: 'e2nw-user-profile-icon',
  templateUrl: './user-profile-icon.component.html',
  styleUrls: ['./user-profile-icon.component.less']
})
export class UserProfileIconComponent implements OnInit {
  getState: Observable<any>;
  constructor(
    private store: Store<AppState>) {
    this.getState = this.store.pipe(select(selectProfileState));
  }
  profile: UserProfile;
  ngOnInit() {
    this.getState.subscribe((state: ProfileState) => {
      if (state.profile === null && state.errorMessage === null) {
        this.store.dispatch(new LoadProfile());
      } else {
        this.profile = state.profile;
      }
    });
  }

  logout() {
    this.store.dispatch(new Logout());
  }

}
