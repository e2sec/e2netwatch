import { UserProfile } from '../../models/user-profile';
import { Actions, AuthActionTypes } from '../actions/auth.actions';

export interface AuthState {
    isAuthenticated: boolean;
    user: UserProfile | null;
    errorMessage: string | null;
}

export const initialState: AuthState = {
    isAuthenticated: false,
    user: null,
    errorMessage: null
};

export function reducer(state: AuthState = initialState, action: Actions) {
    switch (action.type) {
        case AuthActionTypes.LOGIN_SUCCESS:
            return {
                ...state,
                isAuthenticated: true,
                user: {
                    token: action.payload.token,
                    email: action.payload.email
                },
                errorMessage: null
            };
        case AuthActionTypes.LOGIN_FAILURE:
            return {
                ...state,
                errorMessage: 'Incorect email or password.'
            };
        case AuthActionTypes.LOGOUT:
            return initialState;
        default:
            return state;
    }
}
