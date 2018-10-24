import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import { AppState, selectProfileState } from '../../../store/app.states';
import { ProfileState } from '../../../store/reducers/profile.reducers';
import { LoadProfile } from '../../../store/actions/profile.actions';
import { UserProfile } from '../../../models/user-profile';

@Component({
  selector: 'e2nw-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.less']
})
export class UserProfileComponent implements OnInit {
  getState: Observable<any>;
  profile: UserProfile;
  constructor(private title: Title, private store: Store<AppState>) {
    this.getState = this.store.pipe(select(selectProfileState));
  }

  ngOnInit() {
    this.title.setTitle('Profile');
    this.getState.subscribe((state: ProfileState) => {
      if (state.profile === null && state.errorMessage === null) {
        this.store.dispatch(new LoadProfile());
      } else {
        this.profile = state.profile;
      }
    });

  }

}
