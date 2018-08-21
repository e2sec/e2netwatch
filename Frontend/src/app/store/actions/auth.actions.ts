import { Action } from '@ngrx/store';
import { LoginModel } from '../../models/login-model';

export enum AuthActionTypes {
    LOGIN = '[AUTH] Login',
    LOGIN_SUCCESS = '[AUTH] Login Success',
    LOGIN_FAILURE = '[AUTH] Login Failure',
    LOGOUT = '[AUTH] Logout',
}

export class Login implements Action {
    readonly type = AuthActionTypes.LOGIN;

    constructor(public payload: LoginModel) { }
}

export class LoginSuccess implements Action {
    readonly type = AuthActionTypes.LOGIN_SUCCESS;

    constructor(public payload: any) { }
}

export class LoginFailure implements Action {
    readonly type = AuthActionTypes.LOGIN_FAILURE;

    constructor(public payload: any) { }
}

export class Logout implements Action {
    readonly type = AuthActionTypes.LOGOUT;
}

export type Actions =
    | Login
    | LoginSuccess
    | LoginFailure
    | Logout;
