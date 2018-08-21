import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../../models/user-profile';
import { AppState, selectAuthState } from '../../../store/app.states';
import { Store } from '@ngrx/store';
import { Logout } from '../../../store/actions/auth.actions';
import { Observable } from 'rxjs';
import { AuthState } from '../../../store/reducers/auth.reducers';

@Component({
  selector: 'e2nw-user-profile-icon',
  templateUrl: './user-profile-icon.component.html',
  styleUrls: ['./user-profile-icon.component.less']
})
export class UserProfileIconComponent implements OnInit {
  getState: Observable<any>;
  constructor(
    private store: Store<AppState>) {
    this.getState = this.store.select(selectAuthState);
  }
  profile: UserProfile;
  ngOnInit() {
    this.getState.subscribe((state: AuthState) => {
      console.log(state);
      this.profile = state.user;
    });
  }

  logout() {
    this.store.dispatch(new Logout());
  }

}
