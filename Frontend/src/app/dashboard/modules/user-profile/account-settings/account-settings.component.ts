import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AppState, selectProfileState } from '../../../../store/app.states';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { ProfileState } from '../../../../store/reducers/profile.reducers';
import { LoadProfile } from '../../../../store/actions/profile.actions';
import { UserProfile } from '../../../../models/user-profile';

@Component({
  selector: 'e2nw-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.less']
})
export class AccountSettingsComponent implements OnInit {
  profileForm: FormGroup;
  getState: Observable<any>;
  constructor(
    private store: Store<AppState>) {
    this.getState = this.store.select(selectProfileState);
  }

  ngOnInit() {
    this.createForm();
    this.getState.subscribe((state: ProfileState) => {
      if (state.profile === null && state.errorMessage === null) {
        this.store.dispatch(new LoadProfile());
      } else {
        this.populateForm(state.profile);
      }
    });
  }
  private createForm() {
    this.profileForm = new FormGroup({
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      newPassword: new FormControl('', []),
      currentPassword: new FormControl('', [Validators.required])
    });
  }

  private populateForm(profile: UserProfile) {
    this.profileForm.setValue(
      {
        'firstname': profile.firstName,
        'lastname': profile.lastName,
        'email': profile.email,
        'newPassword': '',
        'currentPassword': ''
      });
  }

  updateAccount() {
    if (this.profileForm.valid) {
      console.log(this.profileForm.value);
    }
  }
}
