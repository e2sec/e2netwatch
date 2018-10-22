import { UserProfile } from '../../models/user-profile';
import { Actions, AuthActionTypes } from '../actions/auth.actions';

export interface AuthState {
    errorMessage: string | null;
}

export const initialState: AuthState = {
    errorMessage: null
};

export function reducer(state: AuthState = initialState, action: Actions) {
    switch (action.type) {
        case AuthActionTypes.LOGIN_SUCCESS:
            return {
                ...state,
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
