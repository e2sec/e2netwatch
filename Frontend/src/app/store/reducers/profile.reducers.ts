import { UserProfile } from '../../models/user-profile';
import { ProfileActions, UserProfileActionTypes } from '../actions/profile.actions';

export interface ProfileState {
    profile: UserProfile | null;
    errorMessage: string | null;
}

export const initialState: ProfileState = {
    profile: null,
    errorMessage: null
};

export function reducer(state: ProfileState = initialState, action: ProfileActions) {
    switch (action.type) {
        case UserProfileActionTypes.LOAD_SUCCESS:
            const avatarUrl = 'https://ui-avatars.com/api/?name=' +
                action.payload.profile.firstName + '+' +
                action.payload.profile.lastName;
            return {
                // TODO:Remove fake avatar url
                ...state,
                profile: {
                    ...action.payload.profile,
                    avatarUrl: avatarUrl
                },
                errorMessage: null
            };
        case UserProfileActionTypes.LOAD_FAILURE:
            return {
                ...state,
                profile: null,
                errorMessage: 'Failed to load user profile'
            };
        default:
            return state;
    }
}
