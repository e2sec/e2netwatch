import { Injectable } from '@angular/core';
import { UserProfileActionTypes, LoadProfile, LoadSuccess, LoadFailure } from '../actions/profile.actions';
import { Effect, Actions } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import { map, switchMap, catchError } from 'rxjs/operators';
import { ApiService } from '../../services/api.service';

@Injectable()
export class ProfileEffects {

    constructor(
        private actions: Actions,
        private apiService: ApiService
    ) {

    }

    @Effect()
    LoadProfile: Observable<any> = this.actions.ofType(UserProfileActionTypes.LOAD_PROFILE)
        .pipe(switchMap(() => {
            return this.apiService.get('profile')
                .pipe(
                    map((profileData) => {
                        return new LoadSuccess({ profile: profileData });
                    }),
                    catchError((error) => {
                        return of(new LoadFailure({ error: error }));
                    }));
        }));
}
