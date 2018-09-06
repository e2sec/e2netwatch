import * as auth from './reducers/auth.reducers';
import * as profile from './reducers/profile.reducers';
import { createFeatureSelector } from '@ngrx/store';
import { Profile } from 'selenium-webdriver/firefox';

export interface AppState {
    authState: auth.AuthState;
    profileState: profile.ProfileState;
}

export const reducers = {
    auth: auth.reducer,
    profile: profile.reducer
};

export const selectAuthState = createFeatureSelector<AppState>('auth');
