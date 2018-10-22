import { Injectable } from '@angular/core';
import { UserProfileActionTypes, LoadProfile, LoadSuccess, LoadFailure } from '../actions/profile.actions';
import { Effect, Actions, ofType } from '@ngrx/effects';
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
    LoadProfile: Observable<any> = this.actions.pipe(
        ofType(UserProfileActionTypes.LOAD_PROFILE),
        switchMap(() => {
            return this.apiService.get('middleware/api/um/users/getCurrent')
                .pipe(
                    map((response) => {
                        return new LoadSuccess({ profile: response.content });
                    }),
                    catchError((error) => {
                        return of(new LoadFailure({ error: error }));
                    }));
        }));
}
