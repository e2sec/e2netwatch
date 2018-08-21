import * as auth from './reducers/auth.reducers';
import { createFeatureSelector } from '@ngrx/store';

export interface AppState {
    authState: auth.AuthState;
}

export const reducers = {
    auth: auth.reducer
};

export const selectAuthState = createFeatureSelector<AppState>('auth');
