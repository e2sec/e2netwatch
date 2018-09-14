import { initialState, reducer } from './profile.reducers';
import { ProfileActions, LoadSuccess, LoadFailure } from '../actions/profile.actions';
import { UserProfile } from '../../models/user-profile';

describe('ProfileReducer', () => {
    describe('undefined action', () => {
        it('should return the default state', () => {
            const action: ProfileActions = {
                type: null,
                payload: null
            };
            const state = reducer(undefined, action);

            expect(state).toBe(initialState);
        });
    });

    describe('LOAD Success action', () => {
        it('should return user profile', () => {
            const profile: UserProfile = {
                firstName: 'John',
                lastName: 'Admin',
                username: 'JohnAdmin',
                email: 'john.admin@e2security.de',
                avatarUrl: 'https://ui-avatars.com/api/?name=John+Admin',
            };
            const action = new LoadSuccess({ profile: profile });
            const state = reducer(initialState, action);

            expect(state.profile.firstName).toBe('John');
            expect(state.profile.lastName).toBe('Admin');
            expect(state.profile.username).toBe('JohnAdmin');
            expect(state.profile.email).toBe('john.admin@e2security.de');
            expect(state.profile.avatarUrl).toBe('https://ui-avatars.com/api/?name=John+Admin');
            expect(state.errorMessage).toBeNull();
        });
    });

    describe('LOGIN Fail action', () => {
        it('should return the authenticated false and error message', () => {
            const action = new LoadFailure({});
            const state = reducer(initialState, action);

            expect(state.profile).toBeNull();
            expect(state.errorMessage).toBe('Failed to load user profile.');
        });
    });
});
