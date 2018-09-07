import { UserProfile } from '../../models/user-profile';
import { Actions, AuthActionTypes } from '../actions/auth.actions';

export interface AuthState {
    isAuthenticated: boolean;
    errorMessage: string | null;
}

export const initialState: AuthState = {
    isAuthenticated: false,
    errorMessage: null
};

export function reducer(state: AuthState = initialState, action: Actions) {
    switch (action.type) {
        case AuthActionTypes.LOGIN_SUCCESS:
            return {
                ...state,
                isAuthenticated: true,
                errorMessage: null
            };
        case AuthActionTypes.LOGIN_FAILURE:
            return {
                ...state,
                errorMessage: 'Incorect username or password.'
            };
        case AuthActionTypes.LOGOUT:
            return initialState;
        default:
            return state;
    }
}
