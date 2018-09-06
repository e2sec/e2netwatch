import { Injectable } from '@angular/core';
import { Actions, UserProfileActionTypes } from '../actions/profile.actions';
import { Effect } from '@ngrx/effects';
import { Observable } from 'rxjs';

@Injectable()
export class ProfileEffects {

    constructor(private actions: Actions) {

    }

    @Effect()
    LoadProfile: Observable<any> =   this.actions.ofType(UserProfileActionTypes.SET_PROFILE)
}