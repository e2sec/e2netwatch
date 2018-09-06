import { Injectable } from '@angular/core';
import { Action } from '@ngrx/store';
import { Router } from '@angular/router';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { map, switchMap, catchError, tap } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { Observable, of } from 'rxjs';
import { AuthActionTypes, Login, LoginSuccess, LoginFailure } from '../actions/auth.actions';


@Injectable()
export class AuthEffects {

    constructor(
        private actions: Actions,
        private authService: AuthService,
        private router: Router,
    ) { }

    @Effect()
    Login: Observable<any> = this.actions.ofType(AuthActionTypes.LOGIN)
        .pipe(map((action: Login) => action.payload))
        .pipe(switchMap(payload => {
            return this.authService.login(payload)
                .pipe(map(
                    (user) => {
                        return new LoginSuccess({ token: user.token, email: payload.username });
                    }),
                    catchError((error) => {
                        return of(new LoginFailure({ error: error }));
                    }));
        }));

    @Effect({ dispatch: false })
    LoginSuccess: Observable<any> = this.actions.pipe(
        ofType(AuthActionTypes.LOGIN_SUCCESS),
        tap((user) => {
            localStorage.setItem('token', user.payload.token);
            this.router.navigateByUrl('/');
        })
    );

    @Effect({ dispatch: false })
    LoginFailure: Observable<any> = this.actions.pipe(
        ofType(AuthActionTypes.LOGIN_FAILURE)
    );

    @Effect({ dispatch: false })
    Logout: Observable<any> = this.actions.pipe(
        ofType(AuthActionTypes.LOGOUT),
        tap(() => {
            localStorage.removeItem('token');
        })
    );
}
