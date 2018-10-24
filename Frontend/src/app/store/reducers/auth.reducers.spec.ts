import { initialState, reducer } from './auth.reducers';
import { Login, Actions, LoginSuccess, LoginFailure, Logout } from '../actions/auth.actions';

describe('AuthReducer', () => {
    describe('undefined action', () => {
        it('should return the default state', () => {
            const action: Actions = {
                type: null,
                payload: null
            };
            const state = reducer(undefined, action);

            expect(state).toBe(initialState);
        });
    });

    describe('LOGIN Success action', () => {
        it('should return the authenticated true', () => {
            const action = new LoginSuccess({});
            const state = reducer(initialState, action);

            expect(state.errorMessage).toBeNull();
        });
    });

    describe('LOGIN Fail action', () => {
        it('should return the authenticated false and error message', () => {
            const action = new LoginFailure({});
            const state = reducer(initialState, action);

            expect(state.errorMessage).toBe('Incorect username or password.');
        });
    });

    describe('LOGOUT action', () => {
        it('should return initial state', () => {
            const action = new Logout();
            const previousState = { ...initialState, isAuthenticated: true };
            const state = reducer(previousState, action);

            expect(state.errorMessage).toBeNull();
        });
    });
});
